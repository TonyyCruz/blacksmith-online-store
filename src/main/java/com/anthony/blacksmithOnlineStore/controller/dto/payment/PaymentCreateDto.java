package com.anthony.blacksmithOnlineStore.controller.dto.payment;

import java.math.BigDecimal;

import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;

public record PaymentCreateDto(
        PaymentMethod method,
        BigDecimal amount,
        DebitDto debit,
        CreditDto credit,
        PixDTO pix,
        BankSlipDto bankSlip
) {
}
