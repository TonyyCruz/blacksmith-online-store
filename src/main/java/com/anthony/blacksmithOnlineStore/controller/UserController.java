package com.anthony.blacksmithOnlineStore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anthony.blacksmithOnlineStore.controller.dto.user.PasswordUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserUpdateDto;
import com.anthony.blacksmithOnlineStore.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "User management")
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  @Operation(summary = "Get self user data")
  public ResponseEntity<UserDto> getCurrentUser() {
    return ResponseEntity.ok(userService.getUser());
  }

  @PutMapping("/me")
  @Operation(summary = "Update all self user data")
  public ResponseEntity<UserDto> updateCurrentUser(@RequestBody @Valid UserUpdateDto updateDto) {
    return ResponseEntity.ok(userService.updateUser(updateDto));
  }

  @PutMapping("/me/password")
  @Operation(summary = "Update self users password")
  public ResponseEntity<Void> updateCurrentUserPassword(
      @RequestBody @Valid PasswordUpdateDto passwordUpdateDto) {
    userService.updatePassword(passwordUpdateDto);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/me")
  @Operation(summary = "Delete self user")
  public ResponseEntity<Void> deleteCurrentUser() {
    userService.deleteUserFromAuth();
    return ResponseEntity.noContent().build();
  }
}
