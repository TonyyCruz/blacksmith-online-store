package com.anthony.blacksmithOnlineStore.helper;

import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
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

//  ==================== SAVES ====================
  public Order saveorder(Order order) {
    return orderRepository.save(order);
  }

  public Item saveItem(Item item) {
    return itemRepository.save(item);
  }

  public Blacksmith saveBlacksmith(Blacksmith blacksmith) {
    return blacksmithRepository.save(blacksmith);
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  //  ==================== GET NEW ====================

  public Order getNewOrder() {
    Order order = new Order();
    order.setUser(findUserById(USER_ID));
    Item item = saveItem(MockItem.newItem(findBlacksmithById(1L)));
    OrderItem orderItem = MockOrderItem.create(item, 1, order);
    order.addOrderItem(orderItem);
    order.recalculateTotal();
    return saveorder(order);
  }

  public Item getNewItem() {
    Item newItem = MockItem.newItem(findBlacksmithById(1L));
    return itemRepository.save(newItem);
  }

  public User getNewUser() {
    return saveUser(MockUser.user());
  }

  //  ==================== FINDERS ====================

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

  public User findUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalStateException("User not found in test DB"));
  }

  //  ==================== OTHERS ====================
  public Order deliveryOrder(Long id) {
    Order order = findOrderById(id);
    order.setDeliveredAt(LocalDateTime.now());
    order.setStatus(OrderStatus.PAYMENT_APPROVED);
    order.setStatus(OrderStatus.SEPARATING);
    order.setStatus(OrderStatus.DISPATCHED);
    order.setStatus(OrderStatus.IN_TRANSIT);
    order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
    order.setStatus(OrderStatus.DELIVERED);
    return saveorder(order);
  }
}
