package com.anthony.blacksmithOnlineStore.exceptions.user;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;
import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(UUID id) {
    super("User not found: " + id);
  }

  public UserNotFoundException(String username) {
    super("User not found: " + username);
  }
}
