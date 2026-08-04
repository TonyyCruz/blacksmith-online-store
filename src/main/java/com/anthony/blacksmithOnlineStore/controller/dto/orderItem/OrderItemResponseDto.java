package com.anthony.blacksmithOnlineStore.controller.dto.orderItem;

import com.anthony.blacksmithOnlineStore.entity.OrderItem;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDto(
  @Schema(example = "26")
  Long id,
  @Schema(example = "6")
  Long orderId,
  @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
  UUID UserId,
  @Schema(example = "1")
  Long productId,
  @Schema(example = "Sun Blade")
  String productName,
  @Schema(example = "254.20")
  BigDecimal basePrice,
  @Schema(example = "220.40")
  BigDecimal priceApplied,
  @Schema(example = "2")
  Integer quantity,
  @Schema(example = "440.80")
  BigDecimal totalPrice) {

  public static OrderItemResponseDto fromEntity(OrderItem orderItem) {
    return new OrderItemResponseDto(
      orderItem.getId(),
      orderItem.getOrder().getId(),
      orderItem.getUserId(),
      orderItem.getItemId(),
      orderItem.getItemName(),
      orderItem.getBasePriceAtPurchase(),
      orderItem.getPriceApplied(),
      orderItem.getQuantity(),
      orderItem.getTotalPrice()
    );
  }
}
