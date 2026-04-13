package com.anthony.blacksmithOnlineStore.controler.dto.user;

import com.anthony.blacksmithOnlineStore.validations.user.PasswordValid;

public record PasswordUpdateDto(
    String currentPassword,
    @PasswordValid
    String newPassword) {

}
