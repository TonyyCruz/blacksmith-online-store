package com.anthony.blacksmithOnlineStore.controller.dto.payment;

import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;
import java.math.BigDecimal;

public record PaymentResponseDto(
    Long id,
    Long orderId,
    PaymentMethod method,
    BigDecimal amount,
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
