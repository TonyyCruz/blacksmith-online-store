package com.anthony.blacksmithOnlineStore.controller.dto.admin;

import com.anthony.blacksmithOnlineStore.enums.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RoleUpdateDto(
    @Schema(description = "The new Role", example = "ADMIN")
    @NotNull Role role
) {}
