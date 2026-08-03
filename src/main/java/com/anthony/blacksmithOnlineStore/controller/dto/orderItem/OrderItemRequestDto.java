package com.anthony.blacksmithOnlineStore.controller.dto.orderItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDto(
  @Schema(description = "The item id", example = "1")
  @NotNull(message = "Item id must not be null")
  @Min(value = 1, message = "invalid item orderId")
  Long itemId,
  @Schema(description = "The item quantity", example = "2")
  @NotNull(message = "Quantity must not be null")
  @Min(value = 1, message = "quantity must be at least 1")
  Integer quantity) {

}
