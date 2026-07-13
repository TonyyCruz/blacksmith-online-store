package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;

public class RatingException extends BadRequestException {
  public RatingException() {
    super("Rating invalid");
  }

  public RatingException(String msg) {
      super(msg);
    }
}
