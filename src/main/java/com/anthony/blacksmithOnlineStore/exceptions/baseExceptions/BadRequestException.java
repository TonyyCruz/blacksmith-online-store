package com.anthony.blacksmithOnlineStore.exceptions.baseExceptions;

public class BadRequestException extends RuntimeException{

  public BadRequestException(String msg, Throwable cause) {
    super(msg, cause);
  }

  protected BadRequestException(String message) {
    super(message);
  }
}
