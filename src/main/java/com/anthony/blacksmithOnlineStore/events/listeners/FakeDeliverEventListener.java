package com.anthony.blacksmithOnlineStore.events.listeners;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.events.ReturnRequestEvent;
import com.anthony.blacksmithOnlineStore.exceptions.DeliverException;
import com.anthony.blacksmithOnlineStore.exceptions.OrderNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FakeDeliverEventListener {
  private final OrderRepository orderRepository;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void deliverRequest(OrderPaidEvent paidEvent) {
    Order order = orderRepository.findById(paidEvent.orderId())
        .orElseThrow(() -> new OrderNotFoundException(paidEvent.orderId()));
    try {
        if (!order.getStatus().equals(OrderStatus.PAYMENT_APPROVED)) {
          throw new DeliverException("A not paid order cannot be delivered");
        }
        if (order.getDeliveredAt() != null) {
          throw new DeliverException("This order has already been delivered");
        }
        order.setDeliveredAt(java.time.LocalDateTime.now());
        order.setStatus(OrderStatus.SEPARATING);
        order.setStatus(OrderStatus.DISPATCHED);
        order.setStatus(OrderStatus.IN_TRANSIT);
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        order.setStatus(OrderStatus.DELIVERED);
        Thread.sleep(5000); // Simulate a delay in the delivery process
        orderRepository.save(order);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new DeliverException("Delivery process was interrupted");
    }
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void returnRequest(ReturnRequestEvent returnEvent) {
    Order order = orderRepository.findById(returnEvent.orderId())
        .orElseThrow(() -> new OrderNotFoundException(returnEvent.orderId()));
    try {
      if (!order.getStatus().equals(OrderStatus.DELIVERED)) {
        throw new DeliverException("A not delivered order cannot be returned");
      }
      order.setStatus(OrderStatus.RETURNED);
        Thread.sleep(5000); // Simulate a delay in the delivery process
        orderRepository.save(order);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new DeliverException("Delivery process was interrupted");
    }
  }
}
