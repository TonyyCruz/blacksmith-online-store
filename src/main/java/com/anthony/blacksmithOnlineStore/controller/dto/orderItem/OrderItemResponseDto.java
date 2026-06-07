package com.anthony.blacksmithOnlineStore.controller.dto.orderItem;

import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDto(
    Long id,
    Long orderId,
    UUID UserId,
    Long productId,
    String productName,
    BigDecimal basePrice,
    BigDecimal priceApplied,
    Integer quantity,
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
