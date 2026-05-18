package com.anthony.blacksmithOnlineStore.security.utils;

import com.anthony.blacksmithOnlineStore.exceptions.UserUnauthorizedException;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserService {

  public  boolean isAdmin() {
    return getAuthentication().getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
  }

  public UUID getAuthenticatedId() {
    return (UUID) getAuthentication().getDetails();
  }

  public String getName() {
    return getAuthentication().getName();
  }

  private Authentication getAuthentication() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) throw new UserUnauthorizedException();
    return auth;
  }
}
