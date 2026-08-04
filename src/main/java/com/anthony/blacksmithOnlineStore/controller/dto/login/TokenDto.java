package com.anthony.blacksmithOnlineStore.controller.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenDto(
  @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI"
    + "6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
  String token, 
  @Schema(example = "user")
  String username, 
  @Schema(example = "CUSTOMER")
  String role) {}
