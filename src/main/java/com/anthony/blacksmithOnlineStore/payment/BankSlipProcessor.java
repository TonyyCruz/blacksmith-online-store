package com.anthony.blacksmithOnlineStore.payment;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;

@Component
public class BankSlipProcessor implements PaymentProcessor {
  private final PaymentMethod paymentMethod = PaymentMethod.BANK_SLIP;

  @Override
  public PaymentResult process(PaymentCreateDto dto) {
      return new PaymentResult(dto.bankSlip().isApproved(), UUID.randomUUID().toString());
  }

  @Override
  public PaymentMethod getPaymentMethod() {
      return paymentMethod;
  }
}
