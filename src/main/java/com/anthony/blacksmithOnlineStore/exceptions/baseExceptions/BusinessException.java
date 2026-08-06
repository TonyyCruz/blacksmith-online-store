package com.anthony.blacksmithOnlineStore.exceptions.baseExceptions;

public class BusinessException extends RuntimeException{
  protected BusinessException(String message) {
    super(message);
  }
}
