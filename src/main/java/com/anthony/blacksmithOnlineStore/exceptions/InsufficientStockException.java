package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BusinessException;

public class InsufficientStockException extends BusinessException {
  public InsufficientStockException(String message) {
    super(message);
  }
}
