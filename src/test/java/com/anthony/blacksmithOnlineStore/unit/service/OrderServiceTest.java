package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
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
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidOrderStatusException;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentNotFoundException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
  private final User user = MockUser.userWithId();

  @Nested
  @DisplayName("Happy Path")
  class OrderServiceHappyPath {

    @Test
    @DisplayName("Should create an order with valid data")
    void create_shouldCreateAnOrderSuccessfully_withCorrectData() {
      OrderRequestDto dto = new OrderRequestDto(List.of(
          new OrderItemRequestDto(1L, 2),
          new OrderItemRequestDto(2L, 2),
          new OrderItemRequestDto(3L, 1)
      ));
      Item item1 = MockItem.item(1L).toBuilder()
          .finalPrice(BigDecimal.valueOf(10)).stock(50).build();
      Item item2 = MockItem.item(2L).toBuilder()
          .finalPrice(BigDecimal.valueOf(20)).stock(50).build();
      Item item3 = MockItem.item(3L).toBuilder()
          .finalPrice(BigDecimal.valueOf(30)).stock(50).build();

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
      when(orderRepository.save(any()))
          .thenAnswer(invocation -> invocation.getArgument(0));

      OrderPaymentDto response = orderService.create(dto);

      assertEquals(BigDecimal.valueOf(90), response.total(),
          "The total must have the correct price");
      assertEquals(OrderStatus.PENDING, response.status(),
          "The status must be pending");
      assertEquals(response.userId(), user.getId(),
          "The userWithId id must be the same");
      assertEquals(3, response.items().size(),
          "The order must have 3 items");
      assertEquals(item1.getId(), response.items().get(0).productId(),
          "The first itemWithId id must be the same");
      assertEquals(item2.getId(), response.items().get(1).productId(),
          "The second itemWithId id must be the same");
      assertEquals(item3.getId(), response.items().get(2).productId(),
          "The third itemWithId id must be the same");
      verify(userService, times(1)).getUserReference();
      verify(itemService, times(1)).findEntityById(item1.getId());
      verify(itemService, times(1)).findEntityById(item2.getId());
      verify(itemService, times(1)).findEntityById(item3.getId());
      verify(orderItemFactory, times(1)).create(item1, 2);
      verify(orderItemFactory, times(1)).create(item2, 2);
      verify(orderItemFactory, times(1)).create(item3, 1);
      verify(orderRepository, times(1)).save(any());
    }

//    @ParameterizedTest
//    @DisplayName("Should set the order status to paid")
//    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#payable")
//    void orderPaid_shouldSetOrderStatusToPaid(OrderStatus status) {
//      Order order = MockOrder.orderWithItems().toBuilder().user(user).status(status).build();
//
//      when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
//      doNothing().when(saleService).performSale(anyLong(), anyInt());
//
//      orderService.orderConfirmed(1L);
//
//      assertEquals(OrderStatus.PAYMENT_APPROVED, order.getStatus(),
//          "The status must be payment approved");
//      verify(orderRepository, times(1)).findById(1L);
//      verify(authUser, times(1)).getAuthenticatedId();
//      verify(saleService, times(order.getOrderItems().size())).performSale(anyLong(), anyInt());
//    }

    @Test
    @DisplayName("Should find a order by id and return a Entity")
    void findEntityById_shouldFindAnOrderByIdSuccessfully_andReturnAEntity() {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      Order response = orderService.getEntityById(order.getId());

      verify(orderRepository, times(1)).findById(order.getId());
      assertEquals(order.getId(), response.getId(), "The id must be the same");
      assertEquals(order.getUser().getId(), response.getUser().getId(), "The userWithId id must be the same");
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
      assertEquals(order.getUser().getId(), response.userId(), "The userWithId id must be the same");
      assertEquals(order.getStatus(), response.status(), "The status must be the same");
      assertEquals(order.getTotal(), response.total(), "The total must be the same");
    }

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#cancelable")
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

//    @ParameterizedTest
//    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#refundable")
//    @DisplayName("Should request a refound of a paid order and set status refound pending")
//    void refundRequest_shouldSetRefoundRequestAndSetStatusRefoundPending(OrderStatus status) {
//      User user = MockUser.userWithId();
//      Order order = MockOrder.orderWithItems()
//          .toBuilder()
//          .user(user)
//          .status(status)
//          .build();
//
//      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
//      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
//      doNothing().when(saleService).cancelSale(anyLong(), anyInt());
//
//      OrderResponseDto response = orderService.refundRequest(order.getId());
//
//      assertEquals(OrderStatus.REFUND_PENDING, response.status(), "Status must be refound pending");
//      verify(orderRepository, times(1)).findById(order.getId());
//      verify(authUser, times(1)).getAuthenticatedId();
//      verify(saleService, times(order.getOrderItems().size())).cancelSale(anyLong(), anyInt());
//    }

    @Test
    @DisplayName("Should request a return of a delivered order")
    void returnRequest_shouldRequestAReturn_ofAReturnedRequestOrder() {
      User user = MockUser.userWithId();
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

//    @Test
//    @DisplayName("Should return an returned order and set status refound pending")
//    void returnComplete_shouldCompleteAReturnedOrder_andSetStatusRefoundPending() {
//      User user = MockUser.userWithId();
//      Order order = MockOrder.orderWithItems()
//          .toBuilder()
//          .user(user)
//          .status(OrderStatus.RETURN_REQUESTED)
//          .build();
//
//      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
//      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
//      doNothing().when(saleService).cancelSale(anyLong(), anyInt());
//
//      OrderResponseDto response = orderService.returnComplete(order.getId());
//
//      assertEquals(OrderStatus.RETURNED, response.status(), "Status must be refound pending");
//      verify(orderRepository, times(1)).findById(order.getId());
//      verify(authUser, times(1)).getAuthenticatedId();
//      verify(saleService, times(order.getOrderItems().size())).cancelSale(anyLong(), anyInt());
//    }

    @Test
    @DisplayName("Get all should get all orders successfully")
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

    @Test
    @DisplayName("Get all should return an empty list when userWithId have no orders")
    void getAll_canReturnAnEmptyOrdersList_whenUserHaveNoOrders() {
      List<Order> orders = new ArrayList<>();

      when(orderRepository.findByUserId(user.getId())).thenReturn(orders);
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      List<OrderResponseDto> responseList = orderService.getUserOrders();

      assertEquals(0, responseList.size(), "Should return an empty list");
      verify(orderRepository, times(1)).findByUserId(user.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

  }

  @Nested
  @DisplayName("Exception Path")
  class OrderServiceExceptionPath {

    @Test
    @DisplayName("Create should throw an exception when itemWithId was no found")
    void create_shouldThrownAnException_whenItemWasNoFound() {
      OrderRequestDto dto = new OrderRequestDto(List.of(
          new OrderItemRequestDto(999L, 2)
      ));

      when(userService.getUserReference()).thenReturn(user);
      when(itemService.findEntityById(999L)).thenThrow(PaymentNotFoundException.class);

      assertThrows(PaymentNotFoundException.class, () -> orderService.create(dto));
      verify(userService, times(1)).getUserReference();
      verify(itemService, times(1)).findEntityById(999L);
    }

//    @Test
//    @DisplayName("Order paid should throw an exception when order was no found")
//    void orderPaid_shouldThrownAnException_whenOrderWasNoFound() {
//      when(orderRepository.findById(999L)).thenReturn(Optional.empty());
//
//      assertThrows(OrderNotFoundException.class, () -> orderService.orderConfirmed(999L));
//      verify(orderRepository, times(1)).findById(999L);
//    }

//    @ParameterizedTest
//    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#nonPayable")
//    @DisplayName("Order paid should throw an exception when try change to uncorrected status")
//    void orderPaid_shouldThrownAnException_whenTryChangeToUncorrectedStatus(OrderStatus status) {
//      Order order = MockOrder.orderWithItems().toBuilder().user(user).status(status).build();
//
//      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
//      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
//
//      assertThrows(InvalidOrderStatusException.class, () -> orderService.orderConfirmed(order.getId()));
//      verify(orderRepository, times(1)).findById(order.getId());
//      verify(authUser, times(1)).getAuthenticatedId();
//    }

//    @Test
//    @DisplayName("Order paid should throw an exception when itemWithId have no stock")
//    void orderPaid_shouldThrownAnException_whenItemHaveNoStock() {
//      Order order = MockOrder.orderWithItems().toBuilder()
//          .status(OrderStatus.PENDING).user(user).build();
//
//      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
//      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
//      doThrow(DataModifyException.class).when(saleService).performSale(anyLong(), anyInt());
//
//      assertThrows(DataModifyException.class, () -> orderService.orderConfirmed(order.getId())
//          , "Must thrown an exception when itemWithId have no stock");
//      verify(authUser, times(1)).getAuthenticatedId();
//      verify(orderRepository, times(1)).findById(order.getId());
//      verify(saleService, times(1)).performSale(anyLong(), anyInt());
//    }

    @Test
    @DisplayName("Cancel should throw an exception when order was no found")
    void cancel_shouldThrownAnException_whenOrderWasNoFound() {
      when(orderRepository.findById(999L)).thenReturn(Optional.empty());

      assertThrows(OrderNotFoundException.class, () -> orderService.cancel(999L));
      verify(orderRepository, times(1)).findById(999L);
    }

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#nonCancelable")
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

      assertThrows(OrderNotFoundException.class, () -> orderService.refundRequest(999L),
          "Must thrown an exception with a non existing order");
      verify(orderRepository, times(1)).findById(999L);
    }

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#nonRefundable")
    @DisplayName("Refound request should throw an exception when order status are incorrect")
    void refoundRequest_shouldThrownAnException_whenOrderStatusAreIncorrect(OrderStatus status) {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).status(status).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(user.getId());

      assertThrows(InvalidOrderStatusException.class,
          () -> orderService.refundRequest(order.getId()));
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
    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#nonReturnable")
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
//    @DisplayName("Return complete should thrown an exception when order was no found")
//    void returnComplete_shouldThrownAnException_whenOrderWasNoFound() {
//      when(orderRepository.findById(999L)).thenReturn(Optional.empty());
//
//      assertThrows(OrderNotFoundException.class, () -> orderService.returnComplete(999L),
//          "Must thrown an exception with a non existing order");
//      verify(orderRepository, times(1)).findById(999L);
//    }

//    @ParameterizedTest
//    @DisplayName("Return complete should throw an exception when order return must not be completed")
//    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#nonReturnCompletable")
//    void returnComplete_shouldThrownAnException_whenOrderReturnMustNotBeCompleted(OrderStatus status) {
//      Order order = MockOrder.orderWithItems().toBuilder().user(user).status(status).build();
//
//      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
//      when(authUser.getAuthenticatedId()).thenReturn(user.getId());
//
//      assertThrows(InvalidOrderStatusException.class,
//          () -> orderService.returnComplete(order.getId()));
//      verify(orderRepository, times(1)).findById(order.getId());
//      verify(authUser, times(1)).getAuthenticatedId();
//    }

    @Test
    @DisplayName("Get by id should thrown an exception when order was no found")
    void getById_shouldThrownAnException_whenOrderWasNoFound() {
      when(orderRepository.findById(999L)).thenReturn(Optional.empty());

      assertThrows(OrderNotFoundException.class, () -> orderService.getById(999L),
          "Must thrown an exception with a non existing order");
      verify(orderRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Get entity by id should thrown an exception when order was no found")
    void getEntityById_shouldThrownAnException_whenOrderWasNoFound() {
      when(orderRepository.findById(999L)).thenReturn(Optional.empty());

      assertThrows(OrderNotFoundException.class, () -> orderService.getEntityById(999L),
          "Must thrown an exception with a non existing order");
      verify(orderRepository, times(1)).findById(999L);
    }

//    @Test
//    @DisplayName("Should thrown an exception trying pay a not authorized order")
//    void orderPaid_shouldThrownAnException_tryingPayANotAuthorizedOrder() {
//      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();
//
//      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
//      when(authUser.getAuthenticatedId()).thenReturn(UUID.randomUUID());
//
//      assertThrows(ForbiddenOperationException.class,
//          () -> orderService.orderConfirmed(order.getId()),
//          "Should thrown an exception trying cancel an unauthorized order");
//      verify(orderRepository, times(1)).findById(order.getId());
//      verify(authUser, times(1)).getAuthenticatedId();
//    }

    @Test
    @DisplayName("Should thrown an exception trying cancel a not authorized order")
    void cancel_shouldThrownAnException_tryingCancelANotAuthorizedOrder() {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(UUID.randomUUID());

      assertThrows(ForbiddenOperationException.class,
          () -> orderService.cancel(order.getId()),
          "Should thrown an exception trying cancel an unauthorized order");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

    @Test
    @DisplayName("Should thrown an exception trying refound request a not authorized order")
    void refoundRequest_shouldThrownAnException_tryingRefoundRequestANotAuthorizedOrder() {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(UUID.randomUUID());

      assertThrows(ForbiddenOperationException.class,
          () -> orderService.refundRequest(order.getId()),
          "Should thrown an exception trying cancel an unauthorized order");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

    @Test
    @DisplayName("Should thrown an exception trying return request a not authorized order")
    void returnRequest_shouldThrownAnException_tryingReturnRequestANotAuthorizedOrder() {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(UUID.randomUUID());

      assertThrows(ForbiddenOperationException.class,
          () -> orderService.returnRequest(order.getId()),
          "Should thrown an exception trying cancel an unauthorized order");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

    @Test
    @DisplayName("Should thrown an exception trying get by id a not authorized order")
    void getById_shouldThrownAnException_tryingGetANotAuthorizedOrder() {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(UUID.randomUUID());

      assertThrows(ForbiddenOperationException.class,
          () -> orderService.getById(order.getId()),
          "Should thrown an exception trying cancel an unauthorized order");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }

    @Test
    @DisplayName("Should thrown an exception trying get entity by id of a not authorized order")
    void getEntityById_shouldThrownAnException_tryingGetEntityOfANotAuthorizedOrder() {
      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();

      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
      when(authUser.getAuthenticatedId()).thenReturn(UUID.randomUUID());

      assertThrows(ForbiddenOperationException.class,
          () -> orderService.getById(order.getId()),
          "Should thrown an exception trying cancel an unauthorized order");
      verify(orderRepository, times(1)).findById(order.getId());
      verify(authUser, times(1)).getAuthenticatedId();
    }
  }

}
