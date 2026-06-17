package com.anthony.blacksmithOnlineStore.controller.dto.payment;

import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;

public record PaymentResponseDto(Long id, PaymentMethod method, String status) {

    public static PaymentResponseDto fromEntity(Payment payment) {
        return new PaymentResponseDto(payment.getId(), payment.getPaymentMethod(), payment.getPaymentStatus().name());
    }
}
