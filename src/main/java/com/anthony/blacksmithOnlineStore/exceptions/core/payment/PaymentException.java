package com.anthony.blacksmithOnlineStore.exceptions.payment;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class PaymentException extends BadRequestException {
  public PaymentException() {
    super("Payment exception");
  }
  public PaymentException(String msg) {
    super(msg);
  }
}
