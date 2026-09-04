package com.anthony.blacksmithOnlineStore.service;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.events.PaymentRefusedEvent;
import com.anthony.blacksmithOnlineStore.exceptions.BusinessViolationException;
import com.anthony.blacksmithOnlineStore.exceptions.ConflictingDataException;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentRefusedException;
import com.anthony.blacksmithOnlineStore.exceptions.ResourceNotFoundException;
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
  private final SaleService saleService;
  private final PaymentRepository paymentRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final FakePaymentProcessorService paymentService;

  @Transactional
  public PaymentResponseDto createPayment(long orderId, PaymentCreateDto dto) {
    try {
      Payment payment = payOrder(orderId, dto);
      eventPublisher.publishEvent(new OrderPaidEvent(orderId, LocalDateTime.now()));
      return PaymentResponseDto.fromEntity(paymentRepository.save(payment));
    } catch(PaymentRefusedException e) {
      eventPublisher.publishEvent(new PaymentRefusedEvent(orderId, dto.method(), dto.amount()));
      throw e;
    }
  }

  private Payment payOrder(long orderId, PaymentCreateDto dto) {
    Order order = orderService.findEntityById(orderId);
    if (order.getPayment() != null) {
      throw new ConflictingDataException(
        "You cannot pay for an order that has already been paid for");
    }
    if (order.getTotal().compareTo(dto.amount()) != 0) {
      throw new BusinessViolationException(
          "The order total price is R$ %.2f but the amount receive is R$ %.2f"
          .formatted(order.getTotal(), dto.amount()));
    }
    decrementStock(order);
    return paymentService.processPayment(order, dto);
  }

  public Payment findEntityById(Long id) {
    return paymentRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Payment not found with id: %d".formatted(id)));
  }

  private void decrementStock(Order order) {
    for (OrderItem orderItem : order.getOrderItems()) {
      saleService.performSale(orderItem.getItemId(), orderItem.getQuantity());
    }
  }
}
