package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class InvalidTokenException extends BadRequestException {
  public InvalidTokenException() {
    super("Invalid or expired token");
  }

  public InvalidTokenException(Throwable cause) {
    super("Invalid or expired token", cause);
  }

  public InvalidTokenException(String msg) {
    super(msg);
  }
}
