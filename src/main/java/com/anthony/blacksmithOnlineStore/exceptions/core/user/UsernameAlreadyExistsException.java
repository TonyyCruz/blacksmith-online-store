package com.anthony.blacksmithOnlineStore.exceptions.core.user;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.BadRequestException;

public class UsernameAlreadyExistsException extends BadRequestException {
  public UsernameAlreadyExistsException() {
    super("Username already exists");
  }
}
