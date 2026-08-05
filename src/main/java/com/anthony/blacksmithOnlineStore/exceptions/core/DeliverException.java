package com.anthony.blacksmithOnlineStore.exceptions.core;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.BadRequestException;

public class DeliverException extends BadRequestException {
  public DeliverException() {
    super("Deliver invalid");
  }

  public DeliverException(String msg) {
      super(msg);
    }
}
