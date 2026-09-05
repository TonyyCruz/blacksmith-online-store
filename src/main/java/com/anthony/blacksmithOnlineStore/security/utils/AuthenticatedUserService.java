package com.anthony.blacksmithOnlineStore.security.utils;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.exceptions.UnauthorizedOperationException;

@Component
public class AuthenticatedUserService {

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
      return SecurityContextHolder.getContext().getAuthentication();
  }

  public boolean isAuthenticated() {
    return getAuthentication() != null && getAuthentication().isAuthenticated();
  }
}
