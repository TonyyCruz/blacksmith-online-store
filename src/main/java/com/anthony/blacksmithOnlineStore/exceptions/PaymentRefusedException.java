package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.PaymentRequiredException;

public class PaymentRefusedException extends PaymentRequiredException {

  public PaymentRefusedException() {
    super("Payment refused");
  }

  public PaymentRefusedException(String message) {
    super(message);
  }
}
