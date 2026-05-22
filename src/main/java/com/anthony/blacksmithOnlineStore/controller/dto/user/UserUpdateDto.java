package com.anthony.blacksmithOnlineStore.controller.dto.user;

import com.anthony.blacksmithOnlineStore.validations.user.Age;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record UserUpdateDto(
    @NotBlank
    String username,
    @Age(min = 18, message = "User must be at least 18 years old.")
    LocalDate birthDate
) {}
