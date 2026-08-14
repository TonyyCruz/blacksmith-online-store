package com.anthony.blacksmithOnlineStore.exceptions.baseExceptions;

public class BusinessException extends RuntimeException{

  public BusinessException(String msg, Throwable cause) {
    super(msg, cause);
  }

  protected BusinessException(String message) {
    super(message);
  }
}
