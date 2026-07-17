package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;

public class PaymentNotFoundException extends NotFoundException {

  public PaymentNotFoundException() {
    super("Payment not found");
  }

  public PaymentNotFoundException(Long id) {
    super("Payment not found: " + id);
  }

  public PaymentNotFoundException(String msg) {
    super(msg);
  }
}
