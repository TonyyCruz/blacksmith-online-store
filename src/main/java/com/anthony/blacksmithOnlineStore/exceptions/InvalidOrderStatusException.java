package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class InvalidItemDataException extends BadRequestException {
  public InvalidItemDataException() {
    super("Invalid item data");
  }
  public InvalidItemDataException(String msg) {
    super(msg);
  }
}
