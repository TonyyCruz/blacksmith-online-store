package com.anthony.blacksmithOnlineStore.controller.docs;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.anthony.blacksmithOnlineStore.controller.docs.anotations.SecuredApiResponses;
import com.anthony.blacksmithOnlineStore.controller.dto.admin.RoleUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admins", description = "Admins management")
public interface AdminControllerDocs {

  @SecuredApiResponses
  @ApiResponse(responseCode = "204", description = "Role updated successfully")
  @Operation(summary = "Update user role, ADMIN only")
  public ResponseEntity<Void> updateUserRole(UUID id, RoleUpdateDto roleUpdateDto);

  @SecuredApiResponses
  @ApiResponse(responseCode = "200", description = "Request completed successfully")
  @Operation(summary = "Find user by id, ADMIN only")
  public ResponseEntity<UserDto> findByUsername(String username);
}
