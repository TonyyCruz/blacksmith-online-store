package com.anthony.blacksmithOnlineStore.controller.dto.orderItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDto(
    @NotNull(message = "Item id must not be null")
    @Min(value = 1, message = "invalid item orderId")
    Long itemId,
    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "quantity must be at least 1")
    Integer quantity) {

}
