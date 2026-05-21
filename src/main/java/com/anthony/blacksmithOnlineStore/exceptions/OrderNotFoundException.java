package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;

public class ItemNotFoundException extends NotFoundException {
  public ItemNotFoundException(Long id) {
    super("Item not found: " + id);
  }
}
