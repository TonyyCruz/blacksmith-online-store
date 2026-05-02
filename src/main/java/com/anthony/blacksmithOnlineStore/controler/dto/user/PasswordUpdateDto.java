package com.anthony.blacksmithOnlineStore.controler.dto.user;

import com.anthony.blacksmithOnlineStore.validations.user.Password;

public record PasswordUpdateDto(
    String currentPassword,
    @Password
    String newPassword) {

}
