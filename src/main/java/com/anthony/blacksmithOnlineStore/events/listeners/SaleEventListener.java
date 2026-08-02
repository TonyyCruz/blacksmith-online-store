package com.anthony.blacksmithOnlineStore.events.listeners;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.events.ItemsReturnedEvent;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import com.anthony.blacksmithOnlineStore.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaleEventListener {
  private final OrderService orderService;
  private final SaleService saleService;

  @EventListener
  public void orderSold(OrderPaidEvent paidEvent) {
    Order order = orderService.findEntityById(paidEvent.orderId());
    for (OrderItem orderItem : order.getOrderItems()) {
      saleService.performSale(orderItem.getItemId(), orderItem.getQuantity());
    }
  }

  @EventListener
  public void returnComplete(ItemsReturnedEvent returnedEvent) {
    for (OrderItem orderItem : returnedEvent.orderItems()) {
      saleService.cancelSale(orderItem.getItemId(), orderItem.getQuantity());
    }
  }
}
