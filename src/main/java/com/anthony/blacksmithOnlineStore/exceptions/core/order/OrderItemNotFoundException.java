package com.anthony.blacksmithOnlineStore.exceptions.core.order;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.NotFoundException;

public class OrderItemNotFoundException extends NotFoundException {
  public OrderItemNotFoundException(Long id) {
    super("Order item not found with orderId: " + id);
  }
}
