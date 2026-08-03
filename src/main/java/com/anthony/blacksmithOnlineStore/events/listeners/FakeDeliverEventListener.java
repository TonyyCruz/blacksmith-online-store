package com.anthony.blacksmithOnlineStore.events.listeners;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.events.ReturnRequestEvent;
import com.anthony.blacksmithOnlineStore.exceptions.DeliverException;
import com.anthony.blacksmithOnlineStore.exceptions.OrderNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FakeDeliverEventListener {
  private final OrderRepository orderRepository;

  @Transactional
  @EventListener
  public void deliverRequest(OrderPaidEvent paidEvent) {
    Order order = orderRepository.findById(paidEvent.orderId())
        .orElseThrow(() -> new OrderNotFoundException(paidEvent.orderId()));
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
      orderRepository.save(order);
  }

  @Transactional
  @EventListener
  public void returnRequest(ReturnRequestEvent returnEvent) {
    Order order = orderRepository.findById(returnEvent.orderId())
        .orElseThrow(() -> new OrderNotFoundException(returnEvent.orderId()));
    try {
      if (!OrderStatus.DELIVERED.equals(order.getStatus())) {
        throw new DeliverException("A not delivered order cannot be returned");
      }
      order.setStatus(OrderStatus.RETURN_REQUESTED);
      Thread.sleep(5000); // Simulate a delay in the delivery process
      order.setStatus(OrderStatus.RETURNED);
      orderRepository.save(order);
    } catch(InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new DeliverException("Delivery process was interrupted");
    }
  }
}
