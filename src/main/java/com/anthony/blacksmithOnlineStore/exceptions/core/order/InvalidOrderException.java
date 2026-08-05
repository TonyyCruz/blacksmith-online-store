package com.anthony.blacksmithOnlineStore.exceptions.core.order;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.BadRequestException;

public class InvalidOrderException extends BadRequestException {
  public InvalidOrderException() {
    super("Invalid order data");
  }
  public InvalidOrderException(String msg) {
    super(msg);
  }
}
