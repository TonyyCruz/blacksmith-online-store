package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentException;
import com.anthony.blacksmithOnlineStore.payment.PaymentProcessor;
import com.anthony.blacksmithOnlineStore.payment.PaymentProcessorFactory;
import com.anthony.blacksmithOnlineStore.payment.PaymentResult;
import com.anthony.blacksmithOnlineStore.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final OrderService orderService;
  private final PaymentRepository paymentRepository;
  private final PaymentProcessorFactory paymentFactory;
  private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public PaymentResponseDto createPayment(long orderId, PaymentCreateDto dto) {
      Order order = orderService.getEntityById(orderId);
      if (order.getTotal().compareTo(dto.amount()) != 0) {
        throw new PaymentException(
            "The order total price is R$ %.2f but the amount receive is R$ %.2f"
            .formatted(order.getTotal(), dto.amount()));
      }
      Payment payment = processPayment(order, dto);
      return PaymentResponseDto.fromEntity(paymentRepository.save(payment));
    }

    private Payment processPayment(Order order, PaymentCreateDto dto) {
      PaymentProcessor processor = paymentFactory.getProcessor(dto.method());
      PaymentResult paymentResult = processor.process(dto);
      Payment payment = PaymentCreateDto.toEntity(dto);
      payment.setTransactionId(paymentResult.transactionId());
      payment.setOrder(order);
      if (paymentResult.isApproved()) {
        payment.setPaymentStatus(PaymentStatus.APPROVED);
        order.setStatus(OrderStatus.PAYMENT_APPROVED);
        eventPublisher.publishEvent(new OrderPaidEvent(order.getId(), LocalDateTime.now()));
      } else {
        payment.setPaymentStatus(PaymentStatus.REJECTED);
        order.setStatus(OrderStatus.PAYMENT_REJECTED);
      }
      return payment;
    }

}
