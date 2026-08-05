package com.anthony.blacksmithOnlineStore.exceptions.user;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class InvalidCredentialsException extends BadRequestException {
  public InvalidCredentialsException() {
    super("Invalid credentials provided");
  }
}
