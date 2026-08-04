package com.anthony.blacksmithOnlineStore.controller.dto.user;

import com.anthony.blacksmithOnlineStore.validations.user.Password;

import io.swagger.v3.oas.annotations.media.Schema;

public record PasswordUpdateDto(
    @Schema(description = "The current password", example = "P4ssw0rd#")
    String currentPassword,
    @Schema(description = "The new password", example = "MyNEwp4ss*")
    @Password
    String newPassword) {

}
