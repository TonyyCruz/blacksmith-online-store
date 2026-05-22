package com.anthony.blacksmithOnlineStore.controller.dto.user;

import com.anthony.blacksmithOnlineStore.entity.User;
import java.util.UUID;

public record UserDto(UUID id, String username, String role, String birthDate) {

  public static UserDto fromEntity(User user) {
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getRole().name(),
        user.getBirthDate().toString()
    );
  }

}
