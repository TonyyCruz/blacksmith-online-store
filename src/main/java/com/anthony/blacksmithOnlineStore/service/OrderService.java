package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controler.dto.Order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controler.dto.Order.OrderResponseDto;
import com.anthony.blacksmithOnlineStore.controler.dto.OrderItem.OrderItemRequestDto;
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
  public OrderResponseDto create(OrderRequestDto dto) {
    Order order = new Order();
    User user = userService.getUserReference();
    order.setUser(user);
    for (OrderItemRequestDto orderItemDto : dto.items()) {
      Item item = itemService.findEntityById(orderItemDto.itemId());
      order.addOrderItem(orderItemFactory.create(item, orderItemDto.quantity()));
    }
    order.recalculateTotal();
    return OrderResponseDto.fromEntity(orderRepository.save(order));
  }

  @Transactional
  public OrderResponseDto approved(Long id) {
    Order order = getEntityById(id);
    order.setStatus(OrderStatus.PAYMENT_APPROVED);
    for (OrderItem orderItem : order.getOrderItems()) {
      saleService.performSale(orderItem.getItemId(), orderItem.getQuantity());
    }
    return OrderResponseDto.fromEntity(order);
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
    order.setStatus(OrderStatus.CANCELLED);
    for (OrderItem orderItem : order.getOrderItems()) {
      saleService.cancelSale(orderItem.getItemId(), orderItem.getQuantity());
    }
    return OrderResponseDto.fromEntity(order);
  }

  public Order getEntityById(Long id) {
    return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
  }
}
