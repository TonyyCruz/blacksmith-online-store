package com.anthony.blacksmithOnlineStore.exceptions.order;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class InvalidOrderStatusException extends BadRequestException {
  public InvalidOrderStatusException() {
    super("Invalid Order Status");
  }
  public InvalidOrderStatusException(String msg) {
    super(msg);
  }
}
