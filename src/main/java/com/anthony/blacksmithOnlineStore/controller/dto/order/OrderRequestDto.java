package com.anthony.blacksmithOnlineStore.controller.dto.order;

import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record OrderRequestDto(
    @NotNull(message = "Items must not be null")
    @NotEmpty(message = "Items must not be empty")
    List<@Valid OrderItemRequestDto> items) {
}
