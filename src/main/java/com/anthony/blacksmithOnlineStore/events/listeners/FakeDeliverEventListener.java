package com.anthony.blacksmithOnlineStore.events.listeners;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.events.ReturnRequestEvent;
import com.anthony.blacksmithOnlineStore.exceptions.BusinessViolationException;
import com.anthony.blacksmithOnlineStore.exceptions.core.order.OrderNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
        throw new BusinessViolationException("A not paid order cannot be delivered");
      }
      if (order.getDeliveredAt() != null) {
        throw new BusinessViolationException("This order has already been delivered");
      }
      order.setDeliveredAt(LocalDateTime.now());
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
    if (!OrderStatus.DELIVERED.equals(order.getStatus())) {
    throw new BusinessViolationException("A not delivered order cannot be returned");
      }
    order.setStatus(OrderStatus.RETURN_REQUESTED);
    order.setStatus(OrderStatus.RETURNED);
    orderRepository.save(order);
  }
}
