package com.anthony.blacksmithOnlineStore.controller.docs;

import org.springframework.http.ResponseEntity;

import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBadRequestDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBadValidationRequestDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiConflictDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiNotFoundDoc;
import com.anthony.blacksmithOnlineStore.controller.dto.login.LoginRequest;
import com.anthony.blacksmithOnlineStore.controller.dto.login.TokenDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication", description = "Sign up and log in")
public interface AuthControllerDocs {

  @ApiResponse(
    responseCode = "201",
    description = "User created successfully",
    content = @Content(
    schema = @Schema(implementation = UserDto.class)))
  @ApiConflictDoc
  @ApiBadValidationRequestDoc
  @Operation(summary = "Register a new user")
  public ResponseEntity<UserDto> register(UserCreateDto userCreateDto);

  @ApiResponse(
    responseCode = "200", 
    description = "Authentication successfully",
    content = @Content(
    schema = @Schema(implementation = TokenDto.class)))
  @ApiNotFoundDoc
  @ApiBadRequestDoc
  @ApiBadValidationRequestDoc
  @Operation(summary = "User authentication")
  public ResponseEntity<TokenDto> login(LoginRequest loginRequest);
}
