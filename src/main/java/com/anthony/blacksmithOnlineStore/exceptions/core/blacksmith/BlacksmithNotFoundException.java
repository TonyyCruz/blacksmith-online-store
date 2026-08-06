package com.anthony.blacksmithOnlineStore.exceptions.core.blacksmith;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;

public class BlacksmithNotFoundException extends NotFoundException {
  public BlacksmithNotFoundException(Long id) {
    super("Blacksmith not found: " + id);
  }
}
