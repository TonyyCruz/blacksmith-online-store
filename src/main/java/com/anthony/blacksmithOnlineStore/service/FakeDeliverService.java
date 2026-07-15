package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.exceptions.DeliverException;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class FakeDeliverService {

  private final OrderRepository orderRepository;

  @Async
  @TransactionalEventListener
  public void deliverRequest(OrderPaidEvent paidEvent) {
    Order order = orderRepository.findById(paidEvent.orderId())
        .orElseThrow(() -> new DeliverException("Order not found"));
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
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new DeliverException("Delivery process was interrupted");
    }
  }

  @Async
  @TransactionalEventListener
  public void returnRequest(OrderPaidEvent paidEvent) {
    Order order = orderRepository.findById(paidEvent.orderId())
        .orElseThrow(() -> new DeliverException("Order not found"));
    try {
    if (!order.getStatus().equals(OrderStatus.DELIVERED)) {
      throw new DeliverException("A not delivered order cannot be returned");
    }
    order.setStatus(OrderStatus.RETURN_REQUESTED);
    order.setStatus(OrderStatus.RETURNED);
      Thread.sleep(5000); // Simulate a delay in the delivery process
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new DeliverException("Delivery process was interrupted");
    }
  }
}
