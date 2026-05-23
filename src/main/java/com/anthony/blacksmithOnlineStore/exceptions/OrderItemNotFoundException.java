package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;

public class OrderItemNotFoundException extends NotFoundException {
  public OrderItemNotFoundException(Long id) {
    super("Order item not found with orderId: " + id);
  }
}
