package com.anthony.blacksmithOnlineStore.controller.dto.order;

import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
  @Schema(example = "1")
  Long id,
  @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
  UUID userId,
  @Schema(example = "2026-07-20T14:30:00")
  LocalDateTime createdAt,
  @Schema(example = "2026-07-21T18:45:00")
  LocalDateTime deliveredAt,
  @Schema(example = "DELIVERED")
  OrderStatus status,
  @Schema(description = "List of order items")
  List<OrderItemResponseDto> items,
  @Schema(example = "334.50")
  BigDecimal total) {

  public static OrderResponseDto fromEntity(Order order) {
    return new OrderResponseDto(
      order.getId(),
      order.getUser().getId(),
      order.getCreatedAt(),
      order.getDeliveredAt(),
      order.getStatus(),
      order.getOrderItems().stream().map(OrderItemResponseDto::fromEntity).toList(),
      order.getTotal()
    );
  }
}
