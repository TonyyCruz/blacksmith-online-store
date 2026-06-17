package com.anthony.blacksmithOnlineStore.service;

import org.springframework.stereotype.Service;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.payment.PaymentProcessor;
import com.anthony.blacksmithOnlineStore.payment.PaymentProcessorFactory;
import com.anthony.blacksmithOnlineStore.payment.PaymentResult;
import com.anthony.blacksmithOnlineStore.repository.PaymentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final OrderService orderService;
  private final PaymentRepository paymentRepository;
  private final PaymentProcessorFactory paymentFactory;

  //@Transactional
  //public void approved(Long id) {
  //  orderService.orderPaid(id);
  //}

  @Transactional
    public PaymentResponseDto createPayment(Long orderId, PaymentCreateDto dto) {
    PaymentProcessor processor = paymentFactory.getProcessor(dto.method());
    PaymentResult result = processor.process(dto);
    Payment payment = new Payment();
    payment.setAmount(dto.amount());
    payment.setPaymentMethod(dto.method());
    payment.setTransactionId(result.transactionId());
    if (result.isApproved()) {
      orderService.orderPaid(orderId);
     } else {
      orderService.paymentRefused(orderId);
     }
    return PaymentResponseDto.fromEntity(paymentRepository.save(payment));
    }
}
