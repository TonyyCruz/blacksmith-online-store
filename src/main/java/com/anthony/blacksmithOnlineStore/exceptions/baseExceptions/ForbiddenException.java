package com.anthony.blacksmithOnlineStore.exceptions.baseExceptions;

public class ForbiddenException extends RuntimeException{

  public ForbiddenException(String msg, Throwable cause) {
    super(msg, cause);
  }

  protected ForbiddenException(String message) {
    super(message);
  }
}
