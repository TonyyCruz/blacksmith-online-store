package com.anthony.blacksmithOnlineStore.controller.dto.user;

import com.anthony.blacksmithOnlineStore.validations.user.Password;

public record PasswordUpdateDto(
    String currentPassword,
    @Password
    String newPassword) {

}
