package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.ForbiddenException;

public class ForbiddenOperationException extends ForbiddenException {

  public ForbiddenOperationException() {
    super("You are not authorized to perform this operation.");
  }

  public ForbiddenOperationException(String msg) {
    super(msg);
  }
}
