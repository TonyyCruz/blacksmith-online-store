package com.anthony.blacksmithOnlineStore.exceptions.order;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class InvalidOrderException extends BadRequestException {
  public InvalidOrderException() {
    super("Invalid order data");
  }
  public InvalidOrderException(String msg) {
    super(msg);
  }
}
