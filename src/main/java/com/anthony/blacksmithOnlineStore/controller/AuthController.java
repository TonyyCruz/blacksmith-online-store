package com.anthony.blacksmithOnlineStore.controller;

import com.anthony.blacksmithOnlineStore.controller.dto.user.UserCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserDto;
import com.anthony.blacksmithOnlineStore.controller.dto.login.LoginRequest;
import com.anthony.blacksmithOnlineStore.controller.dto.login.TokenDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.security.TokenService;
import com.anthony.blacksmithOnlineStore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationManager authManager;
  private final TokenService tokenService;
  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserDto> register(@RequestBody @Valid UserCreateDto userCreateDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userCreateDto));
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authManager.authenticate(loginRequest.toAuthentication());
    User user = (User) authentication.getPrincipal();
    String token = tokenService.generateToken(user);
    return ResponseEntity.ok(new TokenDto(token, user.getUsername(), user.getRole().name()));
  }

}
