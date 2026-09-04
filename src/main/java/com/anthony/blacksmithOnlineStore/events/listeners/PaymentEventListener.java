package com.anthony.blacksmithOnlineStore.events.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import com.anthony.blacksmithOnlineStore.events.PaymentRefusedEvent;
import com.anthony.blacksmithOnlineStore.events.RefundRequestEvent;
import com.anthony.blacksmithOnlineStore.exceptions.BusinessViolationException;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import com.anthony.blacksmithOnlineStore.service.PaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
  private final OrderService orderService;
  private final PaymentService paymentService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void refundPayment(RefundRequestEvent refoundEvent) {
    // REFUND PROCESS
    Order order = orderService.findEntityById(refoundEvent.orderId());
    order.setStatus(OrderStatus.REFUNDED);
    if (order.getPayment() == null) throw new BusinessViolationException("This order have no payment");
    Payment payment = paymentService.findEntityById(order.getPayment().getId());
    payment.setPaymentStatus(PaymentStatus.REFOUNDED);
    // SEND MESSAGE
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void paymentRefused(PaymentRefusedEvent refusedEvent) {
    Order order = orderService.findEntityById(refusedEvent.OrderId());
    if (!OrderStatus.PAYMENT_REJECTED.equals(order.getStatus())) {
      order.setStatus(OrderStatus.PAYMENT_REJECTED);
    }
    // SEND MESSAGE
  }
}
