package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controler.dto.Order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controler.dto.OrderItem.OrderItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import com.anthony.blacksmithOnlineStore.service.util.OrderItemFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final UserService userService;
  private final OrderItemFactory orderItemFactory;
  private final ItemService itemService;

  @Transactional
  public Order create(OrderRequestDto dto, Authentication auth) {
    Order order = new Order();
    User user = userService.getUserReferenceFromAuth(auth);
    order.setUser(user);
    for (OrderItemRequestDto orderItemDto : dto.items()) {
      itemService.performSale(orderItemDto.itemId(), orderItemDto.quantity());
      Item item = itemService.getReferenceById(orderItemDto.itemId());
      order.addOrderItem(orderItemFactory.create(orderItemDto, item));
    }
    order.recalculateTotal();
    return orderRepository.save(order);
  }
}
