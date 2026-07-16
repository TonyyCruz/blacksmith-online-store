package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderPaymentDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderResponseDto;
import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.events.ItemsReturnedEvent;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidOrderException;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidOrderStatusException;
import com.anthony.blacksmithOnlineStore.exceptions.OrderNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import com.anthony.blacksmithOnlineStore.security.utils.AuthenticatedUserService;
import com.anthony.blacksmithOnlineStore.service.util.OrderItemFactory;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final UserService userService;
  private final OrderItemFactory orderItemFactory;
  private final SaleService saleService;
  private final ItemService itemService;
  private final AuthenticatedUserService authUser;
  private final TransactionalEventPublisher eventPublisher;

  @Transactional
  public OrderPaymentDto create(OrderRequestDto dto) {
    Order order = new Order();
    User user = userService.getUserReference();
    order.setUser(user);
    for (OrderItemRequestDto orderItemDto : dto.items()) {
      Item item = itemService.findEntityById(orderItemDto.itemId());
      if (!item.isActive()) {
        throw new InvalidOrderException("Item %d is unactive".formatted(item.getId()));
      }
      if (orderItemDto.quantity() > item.getStock()) {
        throw new InvalidOrderException(
            "Item %d does not have enough stock".formatted(item.getId()));
      }
      OrderItem orderItem = orderItemFactory.create(item, orderItemDto.quantity());
      orderItem.setOrder(order);
      orderItem.setUserId(user.getId());
      order.addOrderItem(orderItem);
    }
    order.recalculateTotal();
    return OrderPaymentDto.fromEntity(orderRepository.save(order));
  }

//  @Transactional
//  public void orderConfirmed(long orderId) {
//    Order order = getEntityById(orderId);
//    for (OrderItem orderItem : order.getOrderItems()) {
//      saleService.performSale(orderItem.getItemId(), orderItem.getQuantity());
//    }
    // =========> MODIFICAR PARA ENVIAR UM EVENTO NO LUGAR DE CHAMAR O DELIVER SERVICE <========
//    deliverService.deliverRequest(order);
//  }

  @Transactional
  public OrderResponseDto cancel(long id) {
    Order order = getEntityById(id);
    if (!order.getStatus().canBeCanceled()) {
      throw new InvalidOrderStatusException("Only pending orders can be cancelled.");
    }
    order.setStatus(OrderStatus.CANCELLED);
    return OrderResponseDto.fromEntity(order);
  }

  @Transactional
  public OrderResponseDto refoundRequest(long id) {
    Order order = getEntityById(id);
    if (!order.getStatus().canBeRefunded()) {
      if (order.getStatus().equals(OrderStatus.REFUND_PENDING)) {
        throw new InvalidOrderStatusException("This order is already pending for refund.");
      }
      throw new InvalidOrderStatusException("This order cannot be refunded");
    }
    order.setStatus(OrderStatus.REFUND_PENDING);
    return OrderResponseDto.fromEntity(order);
  }

  @Transactional
  public OrderResponseDto returnRequest(long id) {
    Order order = getEntityById(id);
    if (!order.getStatus().canBeReturned()) {
      throw new InvalidOrderStatusException("Only delivered orders can be returned.");
    }
    order.setStatus(OrderStatus.RETURN_REQUESTED);
    return OrderResponseDto.fromEntity(order);
  }

  public OrderResponseDto getById(long id) {
    return OrderResponseDto.fromEntity(getEntityById(id));
  }

  public List<OrderResponseDto> getUserOrders() {
    return orderRepository.findByUserId(authUser.getAuthenticatedId())
        .stream()
        .map(OrderResponseDto::fromEntity)
        .toList();
  }

  public Order getEntityById(long id) {
    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    if (authUser.isAdmin()) return order;
    if (!order.getUser().getId().equals(authUser.getAuthenticatedId())) {
      throw new ForbiddenOperationException(
          "You cannot cannot access this order."
      );
    }
    return order;
  }
}
