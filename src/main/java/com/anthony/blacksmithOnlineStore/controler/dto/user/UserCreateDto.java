package com.anthony.blacksmithOnlineStore.controler.dto.user;

import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.validations.user.PasswordValid;
import com.anthony.blacksmithOnlineStore.validations.user.ValidAge;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UserCreateDto(
    @NotBlank(message = "Username is required.")
    String username,
    @PasswordValid
    String password,
    @NotNull(message = "Birth date is required.")
    @ValidAge(min = 18, message = "User must be at least 18 years old.")
    LocalDate birthDate) {

  public static User toEntity(UserCreateDto dto) {
    User user = new User();
    user.setUsername(dto.username);
    user.setPassword(dto.password);
    user.setBirthDate(dto.birthDate);
    return user;
  }
}
