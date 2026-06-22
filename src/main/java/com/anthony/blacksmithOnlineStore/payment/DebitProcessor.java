package com.anthony.blacksmithOnlineStore.payment;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;

@Component
public class DebitProcessor implements PaymentProcessor {
  private final PaymentMethod paymentMethod = PaymentMethod.DEBIT_CARD;

  @Override
  public PaymentResult process(PaymentCreateDto dto) {
      return new PaymentResult(dto.debit().isApproved(), UUID.randomUUID().toString());
  }

  @Override
  public PaymentMethod getPaymentMethod() {
      return paymentMethod;
  }
}
