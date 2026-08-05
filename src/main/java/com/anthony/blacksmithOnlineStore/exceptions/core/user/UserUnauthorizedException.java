package com.anthony.blacksmithOnlineStore.exceptions.user;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.UnauthorizedException;

public class UserUnauthorizedException extends UnauthorizedException {
  public UserUnauthorizedException() {
    super("User not authorized to perform this action");
  }
}
