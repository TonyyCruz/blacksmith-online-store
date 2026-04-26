package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controler.dto.admin.RoleUpdateDto;
import com.anthony.blacksmithOnlineStore.controler.dto.user.UserDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.UserNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final UserRepository userRepository;

  public User getEntityById(UUID id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  public void updateRole(UUID id, RoleUpdateDto roleUpdateDto, Authentication auth) {
    UUID currentUserId = (UUID) auth.getDetails();
    if (currentUserId.equals(id)) {
      throw new ForbiddenOperationException("You cannot change your own role.");
    }
    User user = getEntityById(id);
    user.setRole(roleUpdateDto.role());
    userRepository.save(user);
  }

  public UserDto findByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
    return UserDto.fromEntity(user);
  }
}
