package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class DataValidationException extends BadRequestException {
  public DataValidationException(String msg) {
    super(msg);
  }
}
