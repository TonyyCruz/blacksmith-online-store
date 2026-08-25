package com.anthony.blacksmithOnlineStore.controller.dto.payment.methods;

import io.swagger.v3.oas.annotations.media.Schema;

public record DebitDto(
  @Schema(description = "If the payment was approved", example = "false")
  boolean isApproved) {
}
