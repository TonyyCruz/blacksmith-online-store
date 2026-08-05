package com.anthony.blacksmithOnlineStore.exceptions.core.item;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.NotFoundException;

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
