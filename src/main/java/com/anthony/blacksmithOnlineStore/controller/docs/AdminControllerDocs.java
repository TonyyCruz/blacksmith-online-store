package com.anthony.blacksmithOnlineStore.controller.docs;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBadRequestDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBadValidationRequestDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiForbiddenDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiNotFoundDoc;
import com.anthony.blacksmithOnlineStore.controller.dto.admin.RoleUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admins", description = "Admins management")
public interface AdminControllerDocs {

  @ApiNotFoundDoc
  @ApiForbiddenDoc
  @ApiBadRequestDoc
  @ApiBadValidationRequestDoc
  @ApiResponse(responseCode = "204", description = "Role updated successfully")
  @Operation(summary = "Update user role, ADMIN only")
  public ResponseEntity<Void> updateUserRole(UUID id, RoleUpdateDto roleUpdateDto);

  @ApiNotFoundDoc
  @ApiForbiddenDoc
  @ApiResponse(responseCode = "200", description = "Request completed successfully")
  @Operation(summary = "Find user by id, ADMIN only")
  public ResponseEntity<UserDto> findByUsername(String username);
}
