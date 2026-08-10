package com.anthony.blacksmithOnlineStore.controller.dto.login;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.anthony.blacksmithOnlineStore.validations.user.Password;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
  @NotBlank 
  @Schema(description = "The username", example = "Avalon")
  String username, 
  @Schema(description = "The password", example = "Av4l0nGoat3*")
  @Password String password) {

  public UsernamePasswordAuthenticationToken toAuthentication() {
    return new UsernamePasswordAuthenticationToken(username, password);
  }
}
