package com.anthony.blacksmithOnlineStore.exceptions.baseExceptions;

public class NotFoundException extends RuntimeException{
  protected NotFoundException(String message) {
    super(message);
  }
}
