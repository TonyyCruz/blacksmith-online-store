package com.anthony.blacksmithOnlineStore.controller.dto.payment;

import java.math.BigDecimal;

import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;

public record PaymentCreateDto(
        PaymentMethod method,
        BigDecimal amount,
        CardDto card,
        PixDTO pix,
        BankSlipDto bankSlip
) {
}
