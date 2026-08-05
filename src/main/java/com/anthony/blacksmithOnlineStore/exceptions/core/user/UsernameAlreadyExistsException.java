package com.anthony.blacksmithOnlineStore.exceptions.user;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class UsernameAlreadyExistsException extends BadRequestException {
  public UsernameAlreadyExistsException() {
    super("Username already exists");
  }
}
