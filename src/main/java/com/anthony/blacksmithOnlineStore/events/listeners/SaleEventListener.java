package com.anthony.blacksmithOnlineStore.events.listeners;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.events.ItemsReturnedEvent;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import com.anthony.blacksmithOnlineStore.service.SaleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SaleEventListener {
  private final OrderService orderService;
  private final SaleService saleService;

  @Transactional
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void orderSold(OrderPaidEvent paidEvent) {
    Order order = orderService.getEntityById(paidEvent.orderId());
    for (OrderItem orderItem : order.getOrderItems()) {
      saleService.performSale(orderItem.getItemId(), orderItem.getQuantity());
    }
  }

  @Transactional
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void returnComplete(ItemsReturnedEvent returnedEvent) {
    for (OrderItem orderItem : returnedEvent.orderItems()) {
      saleService.cancelSale(orderItem.getItemId(), orderItem.getQuantity());
    }
  }
}
