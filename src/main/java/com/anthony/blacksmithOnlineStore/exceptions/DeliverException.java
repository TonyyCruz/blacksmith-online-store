package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class DeliverException extends BadRequestException {
  public DeliverException() {
    super("Deliver invalid");
  }

  public DeliverException(String msg) {
      super(msg);
    }
}
