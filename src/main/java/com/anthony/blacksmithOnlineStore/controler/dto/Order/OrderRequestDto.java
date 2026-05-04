package com.anthony.blacksmithOnlineStore.controler.dto.Order;

import com.anthony.blacksmithOnlineStore.controler.dto.OrderItem.OrderItemRequestDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderRequestDto(
    @NotNull(message = "Items must not be null")
    @NotEmpty(message = "Items must not be empty")
    List<OrderItemRequestDto> items) {
}
