package com.anthony.blacksmithOnlineStore.exceptions;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;

public class RatingNotFoundException extends NotFoundException {

  public RatingNotFoundException() {
    super("Rating not found");
  }

  public RatingNotFoundException(Long id) {
    super("Rating not found: " + id);
  }

  public RatingNotFoundException(String msg) {
    super(msg);
  }
}
