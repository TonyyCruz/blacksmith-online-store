package com.anthony.blacksmithOnlineStore.controller.dto.order;

import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
    Long id,
    UUID userId,
    LocalDateTime createdAt,
    OrderStatus status,
    List<OrderItemResponseDto> items,
    BigDecimal total) {

  public static OrderResponseDto fromEntity(Order order) {
    List<OrderItemResponseDto> orderItemsDto = order
        .getOrderItems()
        .stream()
        .map(OrderItemResponseDto::fromEntity)
        .toList();
    return new OrderResponseDto(
        order.getId(),
        order.getUser().getId(),
        order.getCreatedAt(),
        order.getStatus(),
        orderItemsDto,
        order.getTotal()
    );
  }
}
