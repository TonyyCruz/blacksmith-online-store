package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {
  public InvalidTokenException() {
    super("Invalid or expired token");
  }

  public InvalidTokenException(Throwable cause) {
    super("Invalid or expired token", cause);
  }
}
