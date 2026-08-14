package com.anthony.blacksmithOnlineStore.exceptions.baseExceptions;

public abstract class UnauthorizedException extends RuntimeException {
  
  protected UnauthorizedException(String msg, Throwable cause) {
    super(msg, cause);
  }

  protected UnauthorizedException(String msg) {
    super(msg);
  }
}
