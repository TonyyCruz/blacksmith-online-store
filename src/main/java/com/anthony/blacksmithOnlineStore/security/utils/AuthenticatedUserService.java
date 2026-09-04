package com.anthony.blacksmithOnlineStore.security.utils;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.exceptions.UnauthorizedOperationException;

@Component
public class AuthenticatedUserService {
  private final Authentication auth;

  public AuthenticatedUserService() {
    this.auth = SecurityContextHolder.getContext().getAuthentication();
  }

  public  boolean isAdmin() {
    return isAuthenticated()
      && getAuthentication()
        .getAuthorities()
        .stream()
        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
  }

  public UUID getAuthenticatedId() {
    return (UUID) getAuthentication().getDetails();
  }

  public String getName() {
    return getAuthentication().getName();
  }

  private Authentication getAuthentication() {
    if (!isAuthenticated()) throw new UnauthorizedOperationException();
    return auth;
  }

  public boolean isAuthenticated() {
    return auth != null || auth.isAuthenticated();
  }
}
