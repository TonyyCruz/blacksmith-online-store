package com.anthony.blacksmithOnlineStore.payment;

import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;
import com.anthony.blacksmithOnlineStore.payment.interface.PaymentProcessor;

@Component
public class CreditCardProcessor implements PaymentProcessor {


    @Override
    public void process(PaymentCreateDto dto) {

        // integração cartão
    }
}
