package com.anthony.blacksmithOnlineStore.controller.dto.orderItem;

import jakarta.validation.constraints.Min;

public record OrderItemRequestDto(
    @Min(value = 1, message = "invalid item id")
    Long itemId,
    @Min(value = 1, message = "quantity must be at least 1")
    Integer quantity) {

}
