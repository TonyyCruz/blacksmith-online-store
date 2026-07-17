package com.anthony.blacksmithOnlineStore.controller.dto.payment;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.BankSlipDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.CreditDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.DebitDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.PixDTO;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import java.math.BigDecimal;

import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;
import lombok.Builder;

@Builder(toBuilder = true)
public record PaymentCreateDto(
        PaymentMethod method,
        BigDecimal amount,
        DebitDto debit,
        CreditDto credit,
        PixDTO pix,
        BankSlipDto bankSlip
) {

  public static Payment toEntity(PaymentCreateDto dto) {
    Payment payment = new Payment();
    payment.setAmount(dto.amount());
    payment.setPaymentMethod(dto.method());
    return payment;
  }
}
