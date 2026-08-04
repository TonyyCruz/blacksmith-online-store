package com.anthony.blacksmithOnlineStore.controller.dto.order;

import java.util.List;

import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemRequestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(
  @Schema(description = "List of order items with their quantities")
  @NotNull(message = "Items must not be null")
  @NotEmpty(message = "Items must not be empty")
  List<@Valid OrderItemRequestDto> items) {
}
