package com.anthony.blacksmithOnlineStore.exceptions.core.user;

import java.util.UUID;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(UUID id) {
    super("User not found: " + id);
  }

  public UserNotFoundException(String username) {
    super("User not found: " + username);
  }
}
