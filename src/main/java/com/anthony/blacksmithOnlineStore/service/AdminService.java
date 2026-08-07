package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controller.dto.admin.RoleUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.ResourceNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.UserRepository;
import com.anthony.blacksmithOnlineStore.security.utils.AuthenticatedUserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final UserRepository userRepository;
  private final AuthenticatedUserService authUser;

  public User findEntityById(UUID id) {
    return userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("User not found with id: %s".formatted(id)));
  }

  public void updateRole(UUID id, RoleUpdateDto roleUpdateDto) {
    if (authUser.getAuthenticatedId().equals(id)) {
      throw new ForbiddenOperationException("You cannot change your own role.");
    }
    User user = findEntityById(id);
    user.setRole(roleUpdateDto.role());
    userRepository.save(user);
  }

  public UserDto findByUsername(String username) {
    User user = userRepository.findByUsername(username).orElseThrow(() ->
        new ResourceNotFoundException("User not found with username: %s".formatted(username)));
    return UserDto.fromEntity(user);
  }
}
