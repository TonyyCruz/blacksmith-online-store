package com.anthony.blacksmithOnlineStore.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.events.ItemsReturnedEvent;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.exceptions.DataModifyException;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidOrderException;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {
  private final ItemService itemService;
  private final ItemRepository itemRepository;
  private final OrderService orderService;

  @Transactional
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void orderSold(OrderPaidEvent paidEvent) {
    Order order = orderService.getEntityById(paidEvent.orderId());
    for (OrderItem orderItem : order.getOrderItems()) {
      performSale(orderItem.getItemId(), orderItem.getQuantity());
    }
  }

  @Transactional
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void returnComplete(ItemsReturnedEvent returnedEvent) {
    for (OrderItem orderItem : returnedEvent.orderItems()) {
      cancelSale(orderItem.getItemId(), orderItem.getQuantity());
    }
  }


  private void performSale(long itemId, int qty) {
    itemService.itemExistesVerifier(itemId);
    if (itemRepository.isItemActive(itemId)) {
      throw new InvalidOrderException("Item %d is unactive".formatted(itemId));
    }
    int modifiedLines = itemRepository.decrementStockAndIncrementSoldQuantity(itemId, qty);
    if (modifiedLines == 0) {
      throw new DataModifyException("Item have no stock for this operation: " + itemId);
    }
  }

  private void cancelSale(long itemId, int qty) {
    itemService.itemExistesVerifier(itemId);
    int modifiedLines = itemRepository.incrementStockAndDecrementSoldQuantity(itemId, qty);
    if (modifiedLines == 0) {
      throw new DataModifyException("Item have no sufficient sold for this operation: " + itemId);
    }
  }
}
