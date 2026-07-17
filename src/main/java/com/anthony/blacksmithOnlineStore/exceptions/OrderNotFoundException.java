package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
  public OrderNotFoundException() {
    super("Order not found");
  }

  public OrderNotFoundException(Long id) {
    super("Order not found: " + id);
  }

  public OrderNotFoundException(String msg) {
    super(msg);
  }
}
