package com.anthony.blacksmithOnlineStore.exceptions.baseExceptions;

public class ForbiddenException extends RuntimeException{
  protected ForbiddenException(String message) {
    super(message);
  }
}
