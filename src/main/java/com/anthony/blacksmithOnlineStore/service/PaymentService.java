package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.events.RefoundRequestEvent;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentException;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentNotFoundException;

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

import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final OrderService orderService;
  private final PaymentRepository paymentRepository;
  private final PaymentProcessorFactory paymentFactory;
  private final TransactionalEventPublisher eventPublisher;

  @Transactional
    public PaymentResponseDto createPayment(long orderId, PaymentCreateDto dto) {
    Order order = orderService.getEntityById(orderId);
    PaymentProcessor processor = paymentFactory.getProcessor(dto.method());
    PaymentResult result = processor.process(dto);
    Payment payment = PaymentCreateDto.toEntity(dto);
    payment.setTransactionId(result.transactionId());
    payment.setOrder(order);
    if (result.isApproved()) {
      order.setStatus(OrderStatus.PAYMENT_APPROVED);
      payment.setPaymentStatus(PaymentStatus.APPROVED);
      eventPublisher.publishEvent(new OrderPaidEvent(orderId, java.time.LocalDateTime.now()));
    } else {
      order.setStatus(OrderStatus.PAYMENT_REJECTED);
      payment.setPaymentStatus(PaymentStatus.REJECTED);
    }
    return PaymentResponseDto.fromEntity(paymentRepository.save(payment));
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    private void refoundPayment(RefoundRequestEvent refoundEvent) {
      // REFOUND PROCCESS
      Order order = orderService.getEntityById(refoundEvent.orderId());
      order.setStatus(OrderStatus.REFUNDED);
      if (order.getPayment() == null) throw new PaymentException("This order have no payment");
      Payment payment = paymentRepository.findById(order.getPayment().getId())
      .orElseThrow(() -> new PaymentNotFoundException(order.getPayment().getId()));
      payment.setPaymentStatus(PaymentStatus.REFOUNDED);
    }
}
