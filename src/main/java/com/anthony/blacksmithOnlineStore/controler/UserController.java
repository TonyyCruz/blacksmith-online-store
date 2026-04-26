package com.anthony.blacksmithOnlineStore.controler;

import com.anthony.blacksmithOnlineStore.controler.dto.user.PasswordUpdateDto;
import com.anthony.blacksmithOnlineStore.controler.dto.user.UserDto;
import com.anthony.blacksmithOnlineStore.controler.dto.user.UserUpdateDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(Authentication auth) {
    User user = userService.getUserEntityFromAuth(auth);
    return ResponseEntity.ok(UserDto.fromEntity(user));
  }

  @PutMapping("/me")
  public ResponseEntity<UserDto> updateCurrentUser(
      @RequestBody @Valid UserUpdateDto updateDto,
      Authentication auth) {
    return ResponseEntity.ok(userService.updateUser(updateDto, auth));
  }

  @PutMapping("/me/password")
  public ResponseEntity<Void> updateCurrentUserPassword(
      @RequestBody @Valid PasswordUpdateDto passwordUpdateDto,
      Authentication auth) {
    userService.updatePassword(passwordUpdateDto, auth);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/me")
  public ResponseEntity<Void> deleteCurrentUser(Authentication auth) {
    userService.deleteUserFromAuth(auth);
    return ResponseEntity.noContent().build();
  }
}
