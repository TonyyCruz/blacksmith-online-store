package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.exceptions.DeliverException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliverService {

  public void deliverRequest(Order order) {
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
  }

  public void returnRequest(Order order) {
    if (!order.getStatus().equals(OrderStatus.DELIVERED)) {
      throw new DeliverException("A not delivered order cannot be returned");
    }
    order.setStatus(OrderStatus.RETURN_REQUESTED);
    order.setStatus(OrderStatus.RETURNED);
  }
}
