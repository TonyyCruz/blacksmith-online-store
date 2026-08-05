package com.anthony.blacksmithOnlineStore.exceptions.core.user;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.ForbiddenException;

public class ForbiddenOperationException extends ForbiddenException {

  public ForbiddenOperationException() {
    super("You are not authorized to perform this operation.");
  }

  public ForbiddenOperationException(String msg) {
    super(msg);
  }
}
