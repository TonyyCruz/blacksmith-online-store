package com.anthony.blacksmithOnlineStore.controller.dto.admin;

import com.anthony.blacksmithOnlineStore.enums.Role;
import jakarta.validation.constraints.NotNull;

public record RoleUpdateDto(
    @NotNull Role role
) {}
