package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BusinessException;

public class BusinessViolationException extends BusinessException {

  public BusinessViolationException() {
    super("Business violation");
  }

  public BusinessViolationException(String message) {
    super(message);
  }
}
