package com.anthony.blacksmithOnlineStore.controller.dto.order;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import java.math.BigDecimal;

public record OrderPaymentDto(Long orderId, OrderStatus status, BigDecimal total) {

  public static OrderPaymentDto fromEntity(Order order) {
    return new OrderPaymentDto(
        order.getId(),
        order.getStatus(),
        order.getTotal()
    );
  }

}
