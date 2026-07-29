package com.anthony.blacksmithOnlineStore.controller.dto.user;

import com.anthony.blacksmithOnlineStore.validations.user.Age;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;

public record UserUpdateDto(
    @NotBlank
    @Length(min = 2, max = 100, message = "Username must be between 2 and 100 characters.")
    String username,
    @Age(min = 18, message = "User must be at least 18 years old.")
    LocalDate birthDate
) {}
