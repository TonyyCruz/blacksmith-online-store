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
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.OrderNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import com.anthony.blacksmithOnlineStore.security.utils.AuthenticatedUserService;
import com.anthony.blacksmithOnlineStore.service.util.OrderItemFactory;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final UserService userService;
  private final OrderItemFactory orderItemFactory;
  private final SaleService saleService;
  private final ItemService itemService;
  private final AuthenticatedUserService authUser;

  @Transactional
  public OrderPaymentDto create(OrderRequestDto dto) {
    Order order = new Order();
    User user = userService.getUserReference();
    order.setUser(user);
    for (OrderItemRequestDto orderItemDto : dto.items()) {
      Item item = itemService.findEntityById(orderItemDto.itemId());
      order.addOrderItem(orderItemFactory.create(item, orderItemDto.quantity()));
    }
    order.recalculateTotal();
    return OrderPaymentDto.fromEntity(orderRepository.save(order));
  }

  @Transactional
  public void orderPaid(Long id) {
    Order order = getEntityById(id);
    order.setStatus(OrderStatus.PAYMENT_APPROVED);
    for (OrderItem orderItem : order.getOrderItems()) {
      saleService.performSale(orderItem.getItemId(), orderItem.getQuantity());
    }
  }

  @Transactional
  public OrderResponseDto cancel(Long id) {
    Order order = getEntityById(id);
    UUID authenticatedId = authUser.getAuthenticatedId();
    if (!order.getUser().getId().equals(authenticatedId)) {
      throw new ForbiddenOperationException(
          "You cannot cancel an order that does not belong to you."
      );
    }
    if (order.getStatus().isPaid()) {
      order.setStatus(OrderStatus.REFUND_PENDING);
      for (OrderItem orderItem : order.getOrderItems()) {
        saleService.cancelSale(orderItem.getItemId(), orderItem.getQuantity());
      }
    }
    else order.setStatus(OrderStatus.CANCELLED);
    return OrderResponseDto.fromEntity(order);
  }

  public Order getEntityById(Long id) {
    return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
  }
}
