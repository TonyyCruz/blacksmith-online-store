package com.anthony.blacksmithOnlineStore.exceptions.baseExceptions;

public abstract class PaymentRequiredException extends RuntimeException {
  
  protected PaymentRequiredException(String msg, Throwable cause) {
    super(msg, cause);
  }

  protected PaymentRequiredException(String msg) {
    super(msg);
  }
}
