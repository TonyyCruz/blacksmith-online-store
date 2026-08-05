package com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions;

public class NotFoundException extends RuntimeException{
  protected NotFoundException(String message) {
    super(message);
  }
}
