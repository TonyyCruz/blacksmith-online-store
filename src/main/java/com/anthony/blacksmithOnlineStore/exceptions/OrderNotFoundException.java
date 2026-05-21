package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
  public OrderNotFoundException(Long id) {
    super("Order not found: " + id);
  }
}
