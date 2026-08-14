package com.anthony.blacksmithOnlineStore.exceptions.baseExceptions;

public abstract class NotFoundException extends RuntimeException{

  public NotFoundException(String msg, Throwable cause) {
    super(msg, cause);
  }

  protected NotFoundException(String message) {
    super(message);
  }
}
