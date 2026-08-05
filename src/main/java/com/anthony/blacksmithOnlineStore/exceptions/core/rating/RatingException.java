package com.anthony.blacksmithOnlineStore.exceptions.core.rating;

import com.anthony.blacksmithOnlineStore.exceptions.core.baseExceptions.BadRequestException;

public class RatingException extends BadRequestException {
  public RatingException() {
    super("Rating invalid");
  }

  public RatingException(String msg) {
      super(msg);
    }
}
