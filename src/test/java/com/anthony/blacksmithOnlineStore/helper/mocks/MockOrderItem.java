package com.anthony.blacksmithOnlineStore.helper.mocks;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.Rating;
import java.math.BigDecimal;
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
        .userId(UUID.randomUUID())
        .blacksmithId(99L)
        .order(order)
        .reviewed(false)
        .build();

    orderItem.calculateTotal();

    return orderItem;
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
