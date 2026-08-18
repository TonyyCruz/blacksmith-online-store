package com.anthony.blacksmithOnlineStore.controller.dto.payment;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.BankSlipDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.CreditDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.DebitDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.PixDTO;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder(toBuilder = true)
public record PaymentCreateDto(
  @Schema(description = "The payment method", example = "CREDIT_CARD")
  @NotNull
  PaymentMethod method,
  @Schema(description = "The payment amount", example = "180.00")
  @NotNull
  @Min(value = 0, message = "Amount must be greater than or equal to zero")
  BigDecimal amount,
  @Schema(description = "The debit payment method, only the payment method used must be inserted")
  DebitDto debit,
  @Schema(description = "The credit payment method, only the payment method used must be inserted")
  CreditDto credit,
  @Schema(description = "The pix payment method, only the payment method used must be inserted")
  PixDTO pix,
  @Schema(description = "The bank slip payment method, only the payment method used must be inserted")
  BankSlipDto bankSlip
) {

  public static Payment toEntity(PaymentCreateDto dto) {
    Payment payment = new Payment();
    payment.setAmount(dto.amount());
    payment.setPaymentMethod(dto.method());
    return payment;
  }
}
