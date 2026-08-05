package com.anthony.blacksmithOnlineStore.exceptions.core;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.InternalException;

public class DataModifyException extends InternalException {
  public DataModifyException(String message) {
    super(message);
  }
}
