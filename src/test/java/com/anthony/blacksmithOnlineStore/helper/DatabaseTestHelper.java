package com.anthony.blacksmithOnlineStore.helper;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockItem;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockOrderItem;
import com.anthony.blacksmithOnlineStore.repository.BlacksmithRepository;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.anthony.blacksmithOnlineStore.repository.OrderItemRepository;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import com.anthony.blacksmithOnlineStore.repository.RatingRepository;
import com.anthony.blacksmithOnlineStore.repository.UserRepository;

@Component
public class DatabaseTestHelper {
  @Autowired
  private OrderItemRepository orderItemRepository;
  @Autowired
  private ItemRepository itemRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private RatingRepository ratingRepository;
  @Autowired
  private BlacksmithRepository blacksmithRepository;
  @Autowired
  private UserRepository userRepository;
  private final UUID USER_ID = UUID.fromString("7b87f809-d142-4dfa-8802-87644d774dd5");

  public Order getNewOrder() {
    Order odr = new Order();
    odr.setUser(findUserById(USER_ID));
    Item itm = itemRepository.save(MockItem.newItem(findBlacksmithById(1L)));
    OrderItem oi = MockOrderItem.create(itm, 1, odr);
    odr.addOrderItem(oi);
    odr.recalculateTotal();
    odr.setDeliveredAt(LocalDateTime.now());
    odr.setStatus(OrderStatus.PAYMENT_APPROVED);
    odr.setStatus(OrderStatus.SEPARATING);
    odr.setStatus(OrderStatus.DISPATCHED);
    odr.setStatus(OrderStatus.IN_TRANSIT);
    odr.setStatus(OrderStatus.OUT_FOR_DELIVERY);
    odr.setStatus(OrderStatus.DELIVERED);
    return orderRepository.save(odr);
  }

  public Item findItemById(Long id) {
    return itemRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(
        "Item not found in test DB"));
  }

  public Order findOrderById(Long id) {
    return orderRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(
        "Order not found in test DB"));
  }

  public Blacksmith findBlacksmithById(Long id) {
    return blacksmithRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Blacksmith not found in test DB"));
  }

  public User findUserById(UUID id) {
    return userRepository.findById(id)
    .orElseThrow(() -> new IllegalArgumentException("User not found in test DB"));
  }

}
