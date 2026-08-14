package com.anthony.blacksmithOnlineStore.exceptions.baseExceptions;

public class ConflictException extends RuntimeException{

  public ConflictException(String msg, Throwable cause) {
    super(msg, cause);
  }

  protected ConflictException(String message) {
    super(message);
  }
}
