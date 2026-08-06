package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.ConflictException;

public class ConflictingDataException extends ConflictException {

  public ConflictingDataException(String msg) {
    super(msg);
  }
}
