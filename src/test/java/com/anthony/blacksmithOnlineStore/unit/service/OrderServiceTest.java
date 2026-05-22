package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void create_shouldCreateAnOrderSuccessfully() {
      OrderRequestDto dto = new OrderRequestDto(List.of(
          new OrderItemRequestDto(1L, 2),
          new OrderItemRequestDto(2L, 2),
          new OrderItemRequestDto(3L, 1)
      ));

      when(userService.getUserReference()).thenReturn(user);
      doNothing().when(saleService).performSale(any(), any());
      when(itemService.findEntityById(any())).thenReturn(MockItem.item());
      when(orderItemFactory.create(any(), any()))
          .thenReturn(MockOrderItem.orderItem());
      when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

      OrderResponseDto response = orderService.create(dto);

      assertEquals(3, response.items().size(), "All orderItems must be saved");
      verify(userService, times(1)).getUserReference();
      verify(saleService, times(1)).performSale(any(), any());
      verify(itemService, times(1)).findEntityById(any());
      verify(orderItemFactory, times(3)).create(any(), any());
      verify(orderRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should cancel an order with valid data")
    void create_shouldCancelAnOrderSuccessfully() {
      User user = MockUser.user();
      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
      doNothing().when(saleService).cancelSale(any(), any());


      OrderResponseDto response = orderService.cancel(order.getId());

      assertEquals(OrderStatus.CANCELLED, response.status(), "Status must be cancelled");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
      verify(saleService, times(1)).cancelSale(any(), any());
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
