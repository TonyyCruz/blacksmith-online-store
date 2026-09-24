package com.anthony.blacksmithOnlineStore.events.listeners;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.events.ReturnRequestEvent;
import com.anthony.blacksmithOnlineStore.exceptions.BusinessViolationException;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FakeDeliverEventListener {
  private final OrderService orderService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deliverRequest(OrderPaidEvent paidEvent) {
    Order order = orderService.findEntityById(paidEvent.orderId());
    if (!order.getStatus().equals(OrderStatus.PAYMENT_APPROVED)) {
      throw new BusinessViolationException("A not paid order cannot be delivered");
    }
    if (order.getDeliveredAt() != null) {
      throw new BusinessViolationException("This order has already been delivered");
    }
    order.setStatus(OrderStatus.SEPARATING);
    order.setStatus(OrderStatus.DISPATCHED);
    order.setStatus(OrderStatus.IN_TRANSIT);
    order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
    order.setStatus(OrderStatus.DELIVERED);
    simulateProcessingTime();
    order.setDeliveredAt(LocalDateTime.now());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void returnRequest(ReturnRequestEvent returnEvent) {
    Order order = orderService.findEntityById(returnEvent.orderId());
    if (!OrderStatus.DELIVERED.equals(order.getStatus())) {
      throw new BusinessViolationException("A not delivered order cannot be returned");
    }
    order.setStatus(OrderStatus.RETURN_REQUESTED);
    order.setStatus(OrderStatus.RETURNED);
    simulateProcessingTime();
  }

  private void simulateProcessingTime() {
    try {
      Thread.sleep(10000); // Simulate some processing time
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
