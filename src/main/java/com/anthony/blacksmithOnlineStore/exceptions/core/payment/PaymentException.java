package com.anthony.blacksmithOnlineStore.exceptions.core.payment;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.BadRequestException;

public class PaymentException extends BadRequestException {
  public PaymentException() {
    super("Payment exception");
  }
  public PaymentException(String msg) {
    super(msg);
  }
}
