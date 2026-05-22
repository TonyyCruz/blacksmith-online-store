package com.anthony.blacksmithOnlineStore.controller.dto.login;

import com.anthony.blacksmithOnlineStore.validations.user.Password;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public record LoginRequest(@NotBlank String username, @Password String password) {

  public UsernamePasswordAuthenticationToken toAuthentication() {
    return new UsernamePasswordAuthenticationToken(username, password);
  }
}
