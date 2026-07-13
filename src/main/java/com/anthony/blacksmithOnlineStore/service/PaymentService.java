package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import org.springframework.stereotype.Service;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
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

  @Transactional
    public PaymentResponseDto createPayment(long orderId, PaymentCreateDto dto) {
    Order order = orderService.getEntityById(orderId);
    PaymentProcessor processor = paymentFactory.getProcessor(dto.method());
    PaymentResult result = processor.process(dto);
    Payment payment = PaymentCreateDto.toEntity(dto);
    payment.setTransactionId(result.transactionId());
    payment.setOrder(order);
    // =========> MODIFICAR PARA ENVIAR UM EVENTO NO LUGAR DE CHAMAR O ORDER SERVICE <========
    if (result.isApproved()) {
      order.setStatus(OrderStatus.PAYMENT_APPROVED);
      payment.setPaymentStatus(PaymentStatus.APPROVED);
      orderService.orderConfirmed(orderId);
    } else {
      order.setStatus(OrderStatus.PAYMENT_REJECTED);
      payment.setPaymentStatus(PaymentStatus.REJECTED);
    }
    return PaymentResponseDto.fromEntity(paymentRepository.save(payment));
    }
}
