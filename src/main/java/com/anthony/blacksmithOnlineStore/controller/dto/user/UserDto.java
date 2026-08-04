package com.anthony.blacksmithOnlineStore.controller.dto.user;

import com.anthony.blacksmithOnlineStore.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserDto(
  @Schema(example = "1")
  UUID id, 
  @Schema(example = "Avalon")
  String username, 
  @Schema(example = "CUSTOMER")
  String role, 
  @Schema(example = "2000-04-06")
  String birthDate) {

  public static UserDto fromEntity(User user) {
    return new UserDto(
      user.getId(),
      user.getUsername(),
      user.getRole().name(),
      user.getBirthDate().toString()
    );
  }

}
