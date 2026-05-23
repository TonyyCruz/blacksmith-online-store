package com.anthony.blacksmithOnlineStore.controller.dto.order;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import java.math.BigDecimal;

public record OrderPayment(Long orderId, OrderStatus status, BigDecimal total) {

  public static OrderPayment fromEntity(Order order) {
    return new OrderPayment(
        order.getId(),
        order.getStatus(),
        order.getTotal()
    );
  }

}
