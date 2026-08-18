package com.anthony.blacksmithOnlineStore.controller.dto.payment;

import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record PaymentResponseDto(
  @Schema(example = "16")
  Long id,
  @Schema(example = "8")
  Long orderId,
  @Schema(example = "DEBIT_CARD")
  PaymentMethod method,
  @Schema(example = "134.20")
  BigDecimal amount,
  @Schema(example = "APPROVED")
  String status) {

  public static PaymentResponseDto fromEntity(Payment payment) {
    return new PaymentResponseDto(
        payment.getId(),
        payment.getOrder().getId(),
        payment.getPaymentMethod(),
        payment.getAmount(),
        payment.getPaymentStatus().name());
    }
}
