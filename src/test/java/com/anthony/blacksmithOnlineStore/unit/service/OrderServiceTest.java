package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
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
import com.anthony.blacksmithOnlineStore.exceptions.InvalidOrderStatusException;
import com.anthony.blacksmithOnlineStore.exceptions.ItemNotFoundException;
import com.anthony.blacksmithOnlineStore.exceptions.OrderNotFoundException;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
      assertEquals(OrderStatus.PENDING, response.status(), "The status must be pending");
      verify(userService, times(1)).getUserReference();
      verify(itemService, times(1)).findEntityById(item1.getId());
      verify(itemService, times(1)).findEntityById(item2.getId());
      verify(itemService, times(1)).findEntityById(item3.getId());
      verify(orderItemFactory, times(1)).create(item1, 2);
      verify(orderItemFactory, times(1)).create(item2, 2);
      verify(orderItemFactory, times(1)).create(item3, 1);
      verify(orderRepository, times(1)).save(any());
    }

    @ParameterizedTest
    @DisplayName("Should set the order status to paid")
    @MethodSource("com.anthony.blacksmithOnlineStore.helper.mocks.OrderStatusHelper#payable")
    void orderPaid_shouldSetOrderStatusToPaid(OrderStatus status) {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).status(status).build();

      when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
      doNothing().when(saleService).performSale(anyLong(), anyInt());

      orderService.orderPaid(1L);

      assertEquals(OrderStatus.PAYMENT_APPROVED, order.getStatus(), "The status must be payment approved");
      verify(orderRepository, times(1)).findById(1L);
      verify(authUser, times(1)).getAuthenticatedId();
      verify(saleService, times(order.getOrderItems().size())).performSale(anyLong(), anyInt());
    }

    @Test
    @DisplayName("Should find a order by id and return a Entity")
    void findEntityById_shouldFindAnOrderByIdSuccessfully_andReturnAEntity() {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      Order response = orderService.getEntityById(order.getId());

      verify(orderRepository, times(1)).findById(order.getId());
      assertEquals(order.getId(), response.getId(), "The id must be the same");
      assertEquals(order.getUser().getId(), response.getUser().getId(), "The user id must be the same");
      assertEquals(order.getStatus(), response.getStatus(), "The status must be the same");
      assertEquals(order.getTotal(), response.getTotal(), "The total must be the same");
    }

    @Test
    @DisplayName("Should find a order by id and return a DTO")
    void findById_shouldFindAnOrderByIdSuccessfully_andReturnADto() {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      OrderResponseDto response = orderService.getById(order.getId());

      verify(orderRepository, times(1)).findById(order.getId());
      assertEquals(order.getId(), response.id(), "The id must be the same");
      assertEquals(order.getUser().getId(), response.userId(), "The user id must be the same");
      assertEquals(order.getStatus(), response.status(), "The status must be the same");
      assertEquals(order.getTotal(), response.total(), "The total must be the same");
    }

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.helper.mocks.OrderStatusHelper#cancelable")
    @DisplayName("Should cancel an not paid order and set status cancelled")
    void cancel_shouldCancelANotPaidOrderSuccessfully_andSetStatusCancelled(OrderStatus status) {
      Order order = MockOrder.orderWithItems()
          .toBuilder()
          .user(user)
          .status(status)
          .build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      OrderResponseDto response = orderService.cancel(order.getId());

      assertEquals(OrderStatus.CANCELLED, response.status());
      verify(orderRepository).findById(order.getId());
      verify(authUser).getAuthenticatedId();
    }

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.helper.mocks.OrderStatusHelper#refundable")
    @DisplayName("Should request a refound of a paid order and set status refound pending")
    void refundRequest_shouldSetRefoundRequestAPaidOrderSuccessfully_andSetStatusRefoundPending(OrderStatus status) {
      User user = MockUser.user();
      Order order = MockOrder.orderWithItems()
          .toBuilder()
          .user(user)
          .status(status)
          .build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
      doNothing().when(saleService).cancelSale(anyLong(), anyInt());

      OrderResponseDto response = orderService.refoundRequest(order.getId());

      assertEquals(OrderStatus.REFUND_PENDING, response.status(), "Status must be refound pending");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
      verify(saleService, times(order.getOrderItems().size())).cancelSale(anyLong(), anyInt());
    }

    @Test
    @DisplayName("Should request a return of a delivered order")
    void returnRequest_shouldRequestAReturn_ofAReturnedRequestOrder() {
      User user = MockUser.user();
      Order order = MockOrder.orderWithItems()
          .toBuilder()
          .user(user)
          .status(OrderStatus.DELIVERED)
          .build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      OrderResponseDto response = orderService.returnRequest(order.getId());

      assertEquals(OrderStatus.RETURN_REQUESTED, response.status(), "Status must be return request");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

    @Test
    @DisplayName("Should return an returned order and set status refound pending")
    void returnComplete_shouldCompleteAReturnedOrder_andSetStatusRefoundPending() {
      User user = MockUser.user();
      Order order = MockOrder.orderWithItems()
          .toBuilder()
          .user(user)
          .status(OrderStatus.RETURN_REQUESTED)
          .build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
      doNothing().when(saleService).cancelSale(anyLong(), anyInt());

      OrderResponseDto response = orderService.returnComplete(order.getId());

      assertEquals(OrderStatus.RETURNED, response.status(), "Status must be refound pending");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
      verify(saleService, times(order.getOrderItems().size())).cancelSale(anyLong(), anyInt());
    }

    @Test
    @DisplayName("Should get all orders successfully")
    void getAll_canReturnAllOrdersSuccessfully() {
      List<Order> orders = List.of(
          MockOrder.deliveredOrder(),
          MockOrder.pendingOrder(),
          MockOrder.orderWithItems()
      );

      when(orderRepository.findByUserId(user.getId())).thenReturn(orders);
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      List<OrderResponseDto> responseList = orderService.getUserOrders();

      assertEquals(3, responseList.size(), "Should return the same number of orders");
      verify(orderRepository, times(1)).findByUserId(user.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

  }

  @Nested
  @DisplayName("Exception Path")
  class SaleServiceExceptionPath {

    @Test
    @DisplayName("Create should throw an exception when item was no found")
    void create_shouldThrownAnException_whenItemWasNoFound() {
      OrderRequestDto dto = new OrderRequestDto(List.of(
          new OrderItemRequestDto(999L, 2)
      ));

      when(userService.getUserReference()).thenReturn(user);
      when(itemService.findEntityById(999L)).thenThrow(ItemNotFoundException.class);

      assertThrows(ItemNotFoundException.class, () -> orderService.create(dto));
      verify(userService, times(1)).getUserReference();
      verify(itemService, times(1)).findEntityById(999L);
    }

    @Test
    @DisplayName("Order paid should throw an exception when order was no found")
    void orderPaid_shouldThrownAnException_whenOrderWasNoFound() {
      when(orderRepository.findById(999L)).thenReturn(Optional.empty());

      assertThrows(OrderNotFoundException.class, () -> orderService.orderPaid(999L));
      verify(orderRepository, times(1)).findById(999L);
    }

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.helper.mocks.OrderStatusHelper#nonPayable")
    @DisplayName("Order paid should throw an exception when order must not be paid")
    void orderPaid_shouldThrownAnException_whenOrderMustNotBePaid(OrderStatus status) {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).status(status).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      assertThrows(InvalidOrderStatusException.class, () -> orderService.orderPaid(order.getId()));
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

    @Test
    @DisplayName("Cancel should throw an exception when order was no found")
    void cancel_shouldThrownAnException_whenOrderWasNoFound() {
      when(orderRepository.findById(999L)).thenReturn(Optional.empty());

      assertThrows(OrderNotFoundException.class, () -> orderService.cancel(999L));
      verify(orderRepository, times(1)).findById(999L);
    }

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.helper.mocks.OrderStatusHelper#nonCancelable")
    @DisplayName("Cancel should throw an exception when order must not be cancelled")
    void cancel_shouldThrownAnException_whenOrderMustNotBeCancelled(OrderStatus status) {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).status(status).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      assertThrows(InvalidOrderStatusException.class, () -> orderService.cancel(order.getId()));
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

    @Test
    @DisplayName("Refound request should thrown an exception when order was no found")
    void refoundRequest_shouldThrownAnException_whenOrderWasNoFound() {
      when(orderRepository.findById(999L)).thenReturn(Optional.empty());

      assertThrows(OrderNotFoundException.class, () -> orderService.refoundRequest(999L),
          "Must thrown an exception with a non existing order");
      verify(orderRepository, times(1)).findById(999L);
    }

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.helper.mocks.OrderStatusHelper#nonRefundable")
    @DisplayName("Refound request should throw an exception when order status are incorrect")
    void refoundRequest_shouldThrownAnException_whenOrderStatusAreIncorrect(OrderStatus status) {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).status(status).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      assertThrows(InvalidOrderStatusException.class,
          () -> orderService.refoundRequest(order.getId()));
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

    @Test
    @DisplayName("Return request should thrown an exception when order was no found")
    void returnRequest_shouldThrownAnException_whenOrderWasNoFound() {
      when(orderRepository.findById(999L)).thenReturn(Optional.empty());

      assertThrows(OrderNotFoundException.class, () -> orderService.returnRequest(999L),
          "Must thrown an exception with a non existing order");
      verify(orderRepository, times(1)).findById(999L);
    }

    @ParameterizedTest
    @DisplayName("Return request should throw an exception when order must not be returned")
    @MethodSource("com.anthony.blacksmithOnlineStore.helper.mocks.OrderStatusHelper#nonReturnable")
    void returnRequest_shouldThrownAnException_whenOrderMustNotBeReturned(OrderStatus status) {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).status(status).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      assertThrows(InvalidOrderStatusException.class,
          () -> orderService.returnRequest(order.getId()));
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

//    @Test
//    @DisplayName("Should throw an exception when item have no stock")
//    void create_shouldThrownAnException_whenItemHaveNoStock() {
//      OrderRequestDto dto = new OrderRequestDto(List.of(
//          new OrderItemRequestDto(1L, 10)
//      ));
//
//      when(userService.getUserReference()).thenReturn(user);
//      doThrow(DataModifyException.class).when(saleService).performSale(any(), any());
//
//      assertThrows(DataModifyException.class, () -> orderService.create(dto));
//      verify(userService, times(1)).getUserReference();
//      verify(saleService, times(1)).performSale(any(), any());
//    }
//
//    @Test
//    @DisplayName("Should thrown an exception trying cancel a not authorized order")
//    void create_shouldThrownAnException_tryingCancelAnNotAuthorizedOrder() {
//      User user = MockUser.user(UUID.randomUUID());
//      Order order = MockOrder.orderWithItems().toBuilder().user(MockUser.user()).build();
//
//      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
//
//      assertThrows(DataException.class,
//          () -> orderService.cancel(order.getId()),"Should thrown an exception trying cancel an unauthorized order");
//      verify(orderRepository, times(1)).findById(order.getId());
//      verify(authUser, times(1)).getAuthenticatedId();
//    }
  }

}
