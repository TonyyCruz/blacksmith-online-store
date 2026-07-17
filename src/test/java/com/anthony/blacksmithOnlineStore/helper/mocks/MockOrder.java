package com.anthony.blacksmithOnlineStore.helper.mocks;

import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import java.math.BigDecimal;
import java.util.List;

public class MockOrder {

  public static Order pendingOrder() {
    User user = MockUser.userWithId();
    return Order.builder()
        .id(1L)
        .user(user)
        .status(OrderStatus.PENDING)
        .total(BigDecimal.ZERO)
        .build();
  }

  public static Order deliveredOrder() {
    User user = MockUser.userWithId();
    return Order.builder()
        .id(2L)
        .user(user)
        .status(OrderStatus.DELIVERED)
        .total(new BigDecimal("240.00"))
        .build();
  }

  public static Order orderWithItems() {
    Order order = pendingOrder();
    OrderItem item1 = MockOrderItem.orderItem(order);
    OrderItem item2 = MockOrderItem.orderItem(order).toBuilder()
        .id(2L)
        .itemId(20L)
        .itemName("Knight Shield")
        .priceApplied(new BigDecimal("90.00"))
        .quantity(1)
        .build();
    //item2.calculateTotal();
    //order.addOrderItem(item1);
    //order.addOrderItem(item2);
    //order.recalculateTotal();
    //return order;

    for (OrderItem orderItem : MockOrderItem.newOrderItems(order)) {
      order.addOrderItem(orderItem);
    }
    order.recalculateTotal();
    return order;
  }

  public static OrderRequestDto orderRequestDto() {
    return new OrderRequestDto(
        List.of(
            new OrderItemRequestDto(1L, 1),
            new OrderItemRequestDto(3L, 2)
        )
    );
  }
}
