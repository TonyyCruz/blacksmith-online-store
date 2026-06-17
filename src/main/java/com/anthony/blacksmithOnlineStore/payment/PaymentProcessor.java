package com.anthony.blacksmithOnlineStore.payment;


import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;

public interface PaymentProcessor {
    PaymentResult process(PaymentCreateDto dto);
    PaymentMethod getPaymentMethod();
}
