package com.anthony.blacksmithOnlineStore.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderResponseDto;
import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.events.RefundRequestEvent;
import com.anthony.blacksmithOnlineStore.events.ReturnRequestEvent;
import com.anthony.blacksmithOnlineStore.exceptions.BusinessViolationException;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.InsufficientStockException;
import com.anthony.blacksmithOnlineStore.exceptions.ResourceNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import com.anthony.blacksmithOnlineStore.security.utils.AuthenticatedUserService;
import com.anthony.blacksmithOnlineStore.service.util.OrderItemFactory;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final UserService userService;
  private final SaleService saleService;
  private final OrderItemFactory orderItemFactory;
  private final ItemService itemService;
  private final AuthenticatedUserService authUser;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public OrderResponseDto create(OrderRequestDto dto) {
    Order order = new Order();
    User user = userService.getUserReference();
    order.setUser(user);
    for (OrderItemRequestDto orderItemDto : dto.items()) {
      Item item = itemService.findEntityById(orderItemDto.itemId());
      if (orderItemDto.quantity() > item.getStock()) {
        throw new InsufficientStockException(
            "Item %d does not have enough stock".formatted(item.getId()));
      }
      OrderItem orderItem = orderItemFactory.create(item, orderItemDto.quantity());
      orderItem.setOrder(order);
      orderItem.setUserId(user.getId());
      order.addOrderItem(orderItem);
    }
    order.recalculateTotal();
    return OrderResponseDto.fromEntity(orderRepository.save(order));
  }

  @Transactional
  public OrderResponseDto cancel(long id) {
    Order order = findEntityById(id);
    if (!order.getStatus().canBeCanceled()) {
      throw new BusinessViolationException("Only pending orders can be cancelled");
    }
    order.setStatus(OrderStatus.CANCELLED);
    return OrderResponseDto.fromEntity(order);
  }

  @Transactional
  public void refundRequest(long id) {
    Order order = findEntityById(id);
    if (order.getStatus().equals(OrderStatus.RETURNED)) {
      restoreStock(order);
      order.setStatus(OrderStatus.REFUND_PENDING);
      eventPublisher.publishEvent(new RefundRequestEvent(id, order.getOrderItems()));
    }
    else if (order.getStatus().equals(OrderStatus.REFUND_PENDING)) {
      eventPublisher.publishEvent(new RefundRequestEvent(id, order.getOrderItems()));
    }
    else {
      throw new BusinessViolationException("This order cannot be refunded");
    }
  }

  @Transactional
  public void returnRequest(long id) {
    Order order = findEntityById(id);
    if (!order.getStatus().canBeReturned()) {
      throw new BusinessViolationException("Only delivered orders can be returned");
    }
    eventPublisher.publishEvent(new ReturnRequestEvent(id, order.getOrderItems()));
  }

  public OrderResponseDto findById(long id) {
    return OrderResponseDto.fromEntity(findEntityById(id));
  }

  public List<OrderResponseDto> getUserOrders() {
    return orderRepository.findByUserId(authUser.getAuthenticatedId())
        .stream()
        .map(OrderResponseDto::fromEntity)
        .toList();
  }

  public Order findEntityById(long id) {
    if (!orderRepository.existsById(id)) {
      throw new ResourceNotFoundException("Order not found with id: " + id);
    }
    if (authUser.isAdmin()) return orderRepository.findById(id).get();
    return orderRepository.findByIdAndUserId(id, authUser.getAuthenticatedId())
        .orElseThrow(() -> new ForbiddenOperationException("You cannot access this order."));
  }

  private void restoreStock(Order order) {
    for (OrderItem orderItem : order.getOrderItems()) {
      saleService.cancelSale(orderItem.getItemId(), orderItem.getQuantity());
    }
  }
}
