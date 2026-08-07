package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.UnauthorizedException;

public class UnauthorizedOperationException extends UnauthorizedException {
  public UnauthorizedOperationException() {
    super("Invalid credentials provided");
  }
}
