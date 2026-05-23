package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderPaymentDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderResponseDto;
import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.exceptions.DataModifyException;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockItem;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockOrder;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockOrderItem;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import com.anthony.blacksmithOnlineStore.security.utils.AuthenticatedUserService;
import com.anthony.blacksmithOnlineStore.service.ItemService;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import com.anthony.blacksmithOnlineStore.service.SaleService;
import com.anthony.blacksmithOnlineStore.service.UserService;
import com.anthony.blacksmithOnlineStore.service.util.OrderItemFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private UserService userService;
  @Mock
  private OrderItemFactory orderItemFactory;
  @Mock
  private SaleService saleService;
  @Mock
  private ItemService itemService;
  @Mock
  private AuthenticatedUserService authUser;
  @InjectMocks
  OrderService orderService;
  private final Item targetItem = MockItem.item();
  private final User user = MockUser.user();

  @Nested
  @DisplayName("Happy Path")
  class SaleServiceHappyPath {

    @Test
    @DisplayName("Should create an order with valid data")
    void create_shouldCreateAnOrderSuccessfully_withCorrectData() {
      OrderRequestDto dto = new OrderRequestDto(List.of(
          new OrderItemRequestDto(1L, 2),
          new OrderItemRequestDto(2L, 2),
          new OrderItemRequestDto(3L, 1)
      ));
      Item item1 = MockItem.item(1L).toBuilder().finalPrice(BigDecimal.valueOf(10)).build();
      Item item2 = MockItem.item(2L).toBuilder().finalPrice(BigDecimal.valueOf(20)).build();
      Item item3 = MockItem.item(3L).toBuilder().finalPrice(BigDecimal.valueOf(30)).build();

      when(userService.getUserReference()).thenReturn(user);
      when(itemService.findEntityById(item1.getId())).thenReturn(item1);
      when(itemService.findEntityById(item2.getId())).thenReturn(item2);
      when(itemService.findEntityById(item3.getId())).thenReturn(item3);
      when(orderItemFactory.create(item1, 2))
          .thenReturn(MockOrderItem.fromItem(item1, 2));
      when(orderItemFactory.create(item2, 2))
          .thenReturn(MockOrderItem.fromItem(item2, 2));
      when(orderItemFactory.create(item3, 1))
          .thenReturn(MockOrderItem.fromItem(item3, 1));
      when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

      OrderPaymentDto response = orderService.create(dto);

      assertEquals(BigDecimal.valueOf(90), response.total(), "The total must have the correct price");
      verify(userService, times(1)).getUserReference();
      verify(itemService, times(1)).findEntityById(item1.getId());
      verify(itemService, times(1)).findEntityById(item2.getId());
      verify(itemService, times(1)).findEntityById(item3.getId());
      verify(orderItemFactory, times(1)).create(item1, 2);
      verify(orderItemFactory, times(1)).create(item2, 2);
      verify(orderItemFactory, times(1)).create(item3, 1);
      verify(orderRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should cancel an not paid order with valid data")
    void cancel_shouldCancelANotPaidOrderSuccessfully_whenItExists() {
      User user = MockUser.user();
      Order order = MockOrder.orderWithItems()
          .toBuilder()
          .user(user)
          .status(OrderStatus.PENDING)
          .build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      OrderResponseDto response = orderService.cancel(order.getId());

      assertEquals(OrderStatus.CANCELLED, response.status(), "Status must be cancelled");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

    @Test
    @DisplayName("Should cancel an paid order with valid data")
    void cancel_shouldCancelAPaidOrderSuccessfully_whenItExists() {
      User user = MockUser.user();
      Order order = MockOrder.orderWithItems()
          .toBuilder()
          .user(user)
          .status(OrderStatus.PAYMENT_APPROVED)
          .build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
      doNothing().when(saleService).cancelSale(anyLong(), anyInt());

      OrderResponseDto response = orderService.cancel(order.getId());

      assertEquals(OrderStatus.REFUND_PENDING, response.status(), "Status must be refound pending");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
      verify(saleService, times(order.getOrderItems().size())).cancelSale(anyLong(), anyInt());
    }

  }

  @Nested
  @DisplayName("Exception Path")
  class SaleServiceExceptionPath {

    @Test
    @DisplayName("Should throw an exception when item have no stock")
    void create_shouldThrownAnException_whenItemHaveNoStock() {
      OrderRequestDto dto = new OrderRequestDto(List.of(
          new OrderItemRequestDto(1L, 10)
      ));

      when(userService.getUserReference()).thenReturn(user);
      doThrow(DataModifyException.class).when(saleService).performSale(any(), any());

      assertThrows(DataModifyException.class, () -> orderService.create(dto));
      verify(userService, times(1)).getUserReference();
      verify(saleService, times(1)).performSale(any(), any());
    }

    @Test
    @DisplayName("Should thrown an exception trying cancel a not authorized order")
    void create_shouldThrownAnException_tryingCancelAnNotAuthorizedOrder() {
      User user = MockUser.user(UUID.randomUUID());
      Order order = MockOrder.orderWithItems().toBuilder().user(MockUser.user()).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

      assertThrows(DataException.class,
          () -> orderService.cancel(order.getId()),"Should thrown an exception trying cancel an unauthorized order");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }
  }

}
