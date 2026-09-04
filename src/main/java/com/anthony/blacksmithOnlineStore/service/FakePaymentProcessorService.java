package com.anthony.blacksmithOnlineStore.service;

import org.springframework.stereotype.Service;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentRefusedException;
import com.anthony.blacksmithOnlineStore.payment.PaymentProcessor;
import com.anthony.blacksmithOnlineStore.payment.PaymentProcessorFactory;
import com.anthony.blacksmithOnlineStore.payment.PaymentResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FakePaymentProcessorService {
  private final PaymentProcessorFactory paymentFactory;

  public Payment processPayment(Order order, PaymentCreateDto dto) {
    PaymentProcessor processor = paymentFactory.getProcessor(dto.method());
    PaymentResult paymentResult = processor.process(dto);
    if (!paymentResult.isApproved()) {
      throw new PaymentRefusedException("The payment was declined");
    }
    Payment payment = PaymentCreateDto.toEntity(dto);
    payment.setTransactionId(paymentResult.transactionId());
    payment.setOrder(order);
    payment.setPaymentStatus(PaymentStatus.APPROVED);
    order.setStatus(OrderStatus.PAYMENT_APPROVED);
    return payment;
  }
}
