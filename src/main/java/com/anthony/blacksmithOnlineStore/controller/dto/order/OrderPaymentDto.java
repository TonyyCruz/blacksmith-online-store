package com.anthony.blacksmithOnlineStore.controller.dto.order;

import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderPaymentDto(
    Long orderId,
    UUID userId,
    List<OrderItemResponseDto> items,
    OrderStatus status,
    BigDecimal total) {

  public static OrderPaymentDto fromEntity(Order order) {
    return new OrderPaymentDto(
        order.getId(),
        order.getUser().getId(),
        order.getOrderItems().stream().map(OrderItemResponseDto::fromEntity).toList(),
        order.getStatus(),
        order.getTotal()
    );
  }

}
