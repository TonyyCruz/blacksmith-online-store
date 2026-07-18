package com.anthony.blacksmithOnlineStore.events.listeners;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import com.anthony.blacksmithOnlineStore.events.RefundRequestEvent;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentException;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.PaymentRepository;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
  private final OrderService orderService;
  private final PaymentRepository paymentRepository;

  @Transactional
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  private void refundPayment(RefundRequestEvent refoundEvent) {
    // REFUND PROCESS
    Order order = orderService.getEntityById(refoundEvent.orderId());
    order.setStatus(OrderStatus.REFUNDED);
    if (order.getPayment() == null) throw new PaymentException("This order have no payment");
    Payment payment = paymentRepository.findById(order.getPayment().getId())
        .orElseThrow(() -> new PaymentNotFoundException(order.getPayment().getId()));
    payment.setPaymentStatus(PaymentStatus.REFOUNDED);
  }
}
