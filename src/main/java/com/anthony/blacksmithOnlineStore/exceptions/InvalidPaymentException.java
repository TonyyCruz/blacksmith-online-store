package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class InvalidPaymentException extends BadRequestException {
  public InvalidPaymentException() {
    super("Invalid Payment");
  }
  public InvalidPaymentException(String msg) {
    super(msg);
  }
}
