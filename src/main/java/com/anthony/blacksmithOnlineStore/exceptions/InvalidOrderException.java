package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class InvalidOrderException extends BadRequestException {
  public InvalidOrderException() {
    super("Invalid order data");
  }
  public InvalidOrderException(String msg) {
    super(msg);
  }
}
