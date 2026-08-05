package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.InternalException;

public class DataModifyException extends InternalException {
  public DataModifyException(String message) {
    super(message);
  }
}
