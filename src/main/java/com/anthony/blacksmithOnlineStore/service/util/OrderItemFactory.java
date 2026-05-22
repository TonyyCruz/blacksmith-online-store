package com.anthony.blacksmithOnlineStore.service.util;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemFactory {

  public OrderItem create(Item item, int quantity) {
    OrderItem orderItem = new OrderItem();
    orderItem.setItemId(item.getId());
    orderItem.setItemName(item.getName());
    orderItem.setBasePriceAtPurchase(item.getBasePrice());
    orderItem.setPriceApplied(item.getFinalPrice());
    orderItem.setQuantity(quantity);
    orderItem.setBlacksmithId(item.getCraftedBy().getId());
    orderItem.calculateTotal();
    return orderItem;
  }
}
