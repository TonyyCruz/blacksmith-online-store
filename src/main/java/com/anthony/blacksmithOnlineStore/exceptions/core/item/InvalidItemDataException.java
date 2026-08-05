package com.anthony.blacksmithOnlineStore.exceptions.core.item;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.BadRequestException;

public class InvalidItemDataException extends BadRequestException {
  public InvalidItemDataException() {
    super("Invalid item data");
  }
  public InvalidItemDataException(String msg) {
    super(msg);
  }
}
