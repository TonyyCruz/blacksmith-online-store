package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;

public class ResourceNotFoundException extends NotFoundException {

  public ResourceNotFoundException(String msg) {
    super(msg);
  }
}
