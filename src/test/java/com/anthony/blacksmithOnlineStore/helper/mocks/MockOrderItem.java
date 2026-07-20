package com.anthony.blacksmithOnlineStore.helper.mocks;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.Rating;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MockOrderItem {

  public static OrderItem orderItem(Order order) {
    OrderItem orderItem = OrderItem.builder()
        .id(1L)
        .itemId(10L)
        .itemName("Dragon Slayer Sword")
        .basePriceAtPurchase(new BigDecimal("150.00"))
        .priceApplied(new BigDecimal("120.00"))
        .quantity(2)
        .userId(order.getUser().getId())
        .blacksmithId(1L)
        .order(order)
        .reviewed(false)
        .build();
    orderItem.calculateTotal();
    return orderItem;
  }

  public static OrderItem fromItem(Item item, int quantity) {
    OrderItem orderItem = OrderItem.builder()
        .itemId(item.getId())
        .itemName(item.getName())
        .basePriceAtPurchase(item.getFinalPrice())
        .priceApplied(item.getFinalPrice())
        .quantity(quantity)
        .userId(UUID.randomUUID())
        .blacksmithId(item.getBlacksmithIdSnapshot())
        .order(new Order())
        .reviewed(false)
        .build();
    orderItem.calculateTotal();
    return orderItem;
  }

  public static OrderItem create(Item item, int quantity, Order order) {
    OrderItem orderItem = OrderItem.builder()
        .id(null)
        .itemId(item.getId())
        .itemName(item.getName())
        .basePriceAtPurchase(item.getFinalPrice())
        .priceApplied(item.getFinalPrice())
        .quantity(quantity)
        .userId(order.getUser().getId())
        .blacksmithId(item.getBlacksmithIdSnapshot())
        .order(order)
        .reviewed(false)
        .build();
    orderItem.calculateTotal();
    return orderItem;
  }

  public static List<OrderItem> newOrderItems(Order order) {
    List<OrderItem> orderItems = new ArrayList<>();
    int quantity = 1;
    for ( Item itm : MockItem.items()) {
      orderItems.add(create(itm, quantity, order));
      quantity++;
    }
    return orderItems;
  }

  public static OrderItem orderItem() {
    return orderItem(MockOrder.orderWithItems());
  }

  public static OrderItem finalizedOrderItem() {
    Order order = MockOrder.deliveredOrder();
    OrderItem orderItem = OrderItem.builder()
        .id(2L)
        .itemId(20L)
        .itemName("Knight Shield")
        .basePriceAtPurchase(new BigDecimal("300.00"))
        .priceApplied(new BigDecimal("250.00"))
        .quantity(1)
        .userId(UUID.randomUUID())
        .blacksmithId(77L)
        .order(order)
        .reviewed(false)
        .build();
    orderItem.calculateTotal();
    return orderItem;
  }

  public static OrderItem reviewedOrderItem() {
    OrderItem orderItem = finalizedOrderItem();
    Rating rating = MockRating.rating(orderItem);
    orderItem.setRating(rating);
    return orderItem;
  }
}
