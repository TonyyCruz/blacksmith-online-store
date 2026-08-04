package com.anthony.blacksmithOnlineStore.controller.dto.user;

import com.anthony.blacksmithOnlineStore.validations.user.Age;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;

public record UserUpdateDto(
  @Schema(description = "The new username", example = "Thor")
  @NotBlank
  @Length(min = 2, max = 100, message = "Username must be between 2 and 100 characters.")
  String username,
  @Schema(description = "The new birthdate", example = "2002-02-05")
  @Age(min = 18, message = "User must be at least 18 years old.")
  LocalDate birthDate
) {}
