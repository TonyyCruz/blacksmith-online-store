package com.anthony.blacksmithOnlineStore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anthony.blacksmithOnlineStore.controller.docs.UserControllerDocs;
import com.anthony.blacksmithOnlineStore.controller.dto.user.PasswordUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserUpdateDto;
import com.anthony.blacksmithOnlineStore.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController implements UserControllerDocs {
  private final UserService userService;

  @Autowired
  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser() {
    return ResponseEntity.ok(userService.getUser());
  }

  @Autowired
  @PutMapping("/me")
  public ResponseEntity<UserDto> updateCurrentUser(@RequestBody @Valid UserUpdateDto updateDto) {
    return ResponseEntity.ok(userService.updateUser(updateDto));
  }

  @Autowired
  @PutMapping("/me/password")
  public ResponseEntity<Void> updateCurrentUserPassword(
      @RequestBody @Valid PasswordUpdateDto passwordUpdateDto) {
    userService.updatePassword(passwordUpdateDto);
    return ResponseEntity.noContent().build();
  }

  @Autowired
  @DeleteMapping("/me")
  public ResponseEntity<Void> deleteCurrentUser() {
    userService.deleteUserFromAuth();
    return ResponseEntity.noContent().build();
  }
}
