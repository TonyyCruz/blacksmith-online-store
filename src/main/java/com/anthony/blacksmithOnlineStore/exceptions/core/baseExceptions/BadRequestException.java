package com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions;

public class BadRequestException extends RuntimeException{
  protected BadRequestException(String message) {
    super(message);
  }
}
