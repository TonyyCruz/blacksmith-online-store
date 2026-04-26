package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controler.dto.user.PasswordUpdateDto;
import com.anthony.blacksmithOnlineStore.controler.dto.user.UserCreateDto;
import com.anthony.blacksmithOnlineStore.controler.dto.user.UserDto;
import com.anthony.blacksmithOnlineStore.controler.dto.user.UserUpdateDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidCredentialsException;
import com.anthony.blacksmithOnlineStore.exceptions.UserNotFoundException;
import com.anthony.blacksmithOnlineStore.exceptions.UsernameAlreadyExistsException;
import com.anthony.blacksmithOnlineStore.repository.UserRepository;
import com.anthony.blacksmithOnlineStore.enums.Role;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDto create(UserCreateDto createDto) {
    if (usernameExists(createDto.username())) throw new UsernameAlreadyExistsException();
    User user = UserCreateDto.toEntity(createDto);
    user.setRole(Role.CUSTOMER);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return UserDto.fromEntity(userRepository.save(user));
  }

  public UserDto updateUser(UserUpdateDto updateDto, Authentication auth) {
    boolean isUsernameChanged = !auth.getName().equals(updateDto.username());
    if (isUsernameChanged && usernameExists(updateDto.username())) {
      throw new UsernameAlreadyExistsException();
    }
    User user = getUserReferenceFromAuth(auth);
    user.setUsername(updateDto.username());
    user.setBirthDate(updateDto.birthDate());
    return UserDto.fromEntity(userRepository.save(user));
  }

  public void updatePassword(PasswordUpdateDto passwordUpdateDto, Authentication auth) {
    User user = getUserEntityFromAuth(auth);
    if (!passwordEncoder.matches(passwordUpdateDto.currentPassword(), user.getPassword())) {
      throw new InvalidCredentialsException();
    }
    user.setPassword(passwordEncoder.encode(passwordUpdateDto.newPassword()));
    userRepository.save(user);
  }

  public User getUserEntityFromAuth(Authentication auth) {
    UUID id = (UUID) auth.getDetails();
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
  }

  public User getUserReferenceFromAuth(Authentication auth) {
    return userRepository.getReferenceById((UUID) auth.getDetails());
  }

  public void deleteUserFromAuth(Authentication auth) {
    userRepository.deleteById((UUID) auth.getDetails());
  }

  private boolean usernameExists(String username) {
    return userRepository.existsByUsername(username);
  }
}
