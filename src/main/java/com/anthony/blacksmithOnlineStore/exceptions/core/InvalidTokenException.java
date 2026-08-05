package com.anthony.blacksmithOnlineStore.exceptions.core;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {
  public InvalidTokenException() {
    super("Invalid or expired token");
  }

  public InvalidTokenException(Throwable cause) {
    super("Invalid or expired token", cause);
  }
}
