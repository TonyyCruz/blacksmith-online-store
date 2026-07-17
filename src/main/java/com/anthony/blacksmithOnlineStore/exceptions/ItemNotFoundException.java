package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;

public class ItemNotFoundException extends NotFoundException {

  public ItemNotFoundException() {
    super("Item not found");
  }

  public ItemNotFoundException(Long id) {
    super("Item not found: " + id);
  }

  public ItemNotFoundException(String msg) {
    super(msg);
  }
}
