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
    simulateProcessingTime(3000L);
    order.setStatus(OrderStatus.SEPARATING);
    simulateProcessingTime(6000L);
    order.setStatus(OrderStatus.DISPATCHED);
    simulateProcessingTime(5000L);
    order.setStatus(OrderStatus.IN_TRANSIT);
    simulateProcessingTime(8000L);
    order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
    simulateProcessingTime(5000L);
    order.setStatus(OrderStatus.DELIVERED);
    order.setDeliveredAt(LocalDateTime.now());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void returnRequest(ReturnRequestEvent returnEvent) {
    Order order = orderService.findEntityById(returnEvent.orderId());
    if (!OrderStatus.RETURN_REQUESTED.equals(order.getStatus())) {
      throw new BusinessViolationException("Only return request order can be returned");
    }
    simulateProcessingTime(8000L);
    order.setStatus(OrderStatus.RETURNED);
  }

  private void simulateProcessingTime(Long miliSec) {
    try {
      Thread.sleep(miliSec); // Simulate some processing time
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
