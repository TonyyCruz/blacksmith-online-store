package com.anthony.blacksmithOnlineStore.payment;

import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;
import com.anthony.blacksmithOnlineStore.payment.interface.PaymentProcessor;

@Component
public class PixProcessor implements PaymentProcessor {

    @Override
    //public PaymentResult process(CreatePaymentDTO dto) {
    public void process(PaymentCreateDto dto) {

        // integração PIX

        //return new PaymentResult(
        //        true,
        //        UUID.randomUUID().toString()
        //);
    }
}
