package com.anthony.blacksmithOnlineStore.events.listeners;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import com.anthony.blacksmithOnlineStore.events.RefundRequestEvent;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentException;
import com.anthony.blacksmithOnlineStore.exceptions.RatingNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.PaymentRepository;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRefoundEventListener {
  private final OrderService orderService;
  private final PaymentRepository paymentRepository;

  @EventListener
  public void refundPayment(RefundRequestEvent refoundEvent) {
    // REFUND PROCESS
    Order order = orderService.findEntityById(refoundEvent.orderId());
    order.setStatus(OrderStatus.REFUNDED);
    if (order.getPayment() == null) throw new PaymentException("This order have no payment");
    Payment payment = paymentRepository.findById(order.getPayment().getId())
        .orElseThrow(() -> new RatingNotFoundException(order.getPayment().getId()));
    payment.setPaymentStatus(PaymentStatus.REFOUNDED);
  }
}
