package com.anthony.blacksmithOnlineStore.security.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

  public static boolean isAdmin() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
  }
}
