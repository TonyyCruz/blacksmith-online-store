package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BusinessException;

public class InsufficientStockException extends BusinessException {

  public InsufficientStockException() {
    super("Insufficient stock for this operation");
  }

  public InsufficientStockException(String message) {
    super(message);
  }
}
