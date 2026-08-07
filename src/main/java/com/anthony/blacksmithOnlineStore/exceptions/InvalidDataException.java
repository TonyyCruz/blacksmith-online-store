package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class InvalidDataException extends BadRequestException {
  public InvalidDataException() {
    super("Invalid data");
  }

  public InvalidDataException(String msg) {
    super(msg);
  }
}
