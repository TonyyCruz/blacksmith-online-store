package com.anthony.blacksmithOnlineStore.controler.dto.user;

import com.anthony.blacksmithOnlineStore.validations.user.ValidAge;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record UserUpdateDto(
    @NotBlank
    String username,
    @ValidAge(min = 18, message = "User must be at least 18 years old.")
    LocalDate birthDate
) {}
