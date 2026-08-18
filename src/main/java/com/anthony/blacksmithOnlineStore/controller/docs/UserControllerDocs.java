package com.anthony.blacksmithOnlineStore.controller.docs;

import org.springframework.http.ResponseEntity;

import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBadRequestDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBadValidationRequestDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiConflictDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiUnauthorizedDoc;
import com.anthony.blacksmithOnlineStore.controller.dto.user.PasswordUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserUpdateDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users")
public interface UserControllerDocs {

  @ApiResponse(
    responseCode = "200",
    description = "User found successfully",
    content = @Content(
    schema = @Schema(implementation = UserDto.class)))
  @ApiUnauthorizedDoc
  @Operation(summary = "Get self user data")
  public ResponseEntity<UserDto> getCurrentUser();

  @ApiResponse(
    responseCode = "200",
    description = "User updated successfully",
    content = @Content(
    schema = @Schema(implementation = UserDto.class)))
  @ApiUnauthorizedDoc
  @ApiConflictDoc
  @ApiBadValidationRequestDoc
  @Operation(summary = "Update all self user data")
  public ResponseEntity<UserDto> updateCurrentUser(UserUpdateDto updateDto);

  @ApiResponse(
    responseCode = "200",
    description = "Password updated successfully",
    content = @Content(
    schema = @Schema(implementation = UserDto.class)))
  @ApiUnauthorizedDoc
  @ApiBadValidationRequestDoc
  @Operation(summary = "Update self users password")
  public ResponseEntity<Void> updateCurrentUserPassword(PasswordUpdateDto passwordUpdateDto);

  @ApiResponse(
    responseCode = "204",
    description = "User deleted successfully")
  @ApiUnauthorizedDoc
  @ApiBadRequestDoc
  @ApiBadValidationRequestDoc
  @Operation(summary = "Delete self user")
  public ResponseEntity<Void> deleteCurrentUser();
}
