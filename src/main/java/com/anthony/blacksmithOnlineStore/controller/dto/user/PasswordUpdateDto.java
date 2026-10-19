package com.anthony.blacksmithOnlineStore.controller.dto.user;

import com.anthony.blacksmithOnlineStore.validations.user.Password;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PasswordUpdateDto(
    @Schema(description = "The current password", example = "P4ssw0rd#")
    @NotNull(message = "Current password must not be null")
    @NotBlank(message = "Current password must not be blank")
    String currentPassword,
    @Schema(description = "The new password", example = "MyNEwp4ss*")
    @Password
    String newPassword) {

}
