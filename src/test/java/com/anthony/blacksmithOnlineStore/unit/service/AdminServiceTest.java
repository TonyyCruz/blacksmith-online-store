package com.anthony.blacksmithOnlineStore.unit.service;

import com.anthony.blacksmithOnlineStore.controller.dto.admin.RoleUpdateDto;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.enums.Role;
import com.anthony.blacksmithOnlineStore.exceptions.UserNotFoundException;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.repository.UserRepository;
import com.anthony.blacksmithOnlineStore.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("Unit test for AdminService")
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private Authentication auth;
  @InjectMocks private AdminService adminService;
  private User targetUser;
  private User adminUser;

  @BeforeEach
  void setup() {
    targetUser = MockUser.user();
    adminUser = MockUser.admin();
  }

  @Nested
  @DisplayName("Happy Path")
  class AdminServiceHappyPath {

    @Test
    @DisplayName("GetById should return user when exists")
    void getById_ShouldReturnUser_WhenExists() {
      when(userRepository.findById(targetUser.getId())).thenReturn(Optional.of(targetUser));
      User found = adminService.getEntityById(targetUser.getId());
      assertEquals(targetUser, found, "The found user should match the target user");
      verify(userRepository, times(1)).findById(targetUser.getId());
    }

    @Test
    @DisplayName("UpdateRole should update user role when username is valid")
    void updateRole_ShouldUpdateUserRole_WhenUsernameIsValid() {
      UUID targetId = targetUser.getId();
      UUID adminId = adminUser.getId();
      RoleUpdateDto dto = new RoleUpdateDto(Role.ADMIN);

      when(userRepository.findById(targetId)).thenReturn(Optional.of(targetUser));
      when(auth.getDetails()).thenReturn(adminId);

      adminService.updateRole(targetId, dto, auth);

      assertEquals(Role.ADMIN, targetUser.getRole());
      verify(userRepository, times(1)).findById(targetId);
      verify(userRepository, times(1)).save(targetUser);
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class AdminServiceExceptionPath {

    @Test
    @DisplayName("UpdateRole should throw UserNotFoundException when user not found")
    void updateRole_ShouldThrowUserNotFoundException_WhenUserNotFound() {
      UUID fakeId = UUID.randomUUID();
      when(userRepository.findById(fakeId)).thenReturn(Optional.empty());
      when(auth.getDetails()).thenReturn(adminUser.getId());
      RoleUpdateDto dto = new RoleUpdateDto(Role.ADMIN);
      assertThrows(UserNotFoundException.class, () -> adminService.updateRole(fakeId, dto, auth));
      verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("UpdateRole should throw UnauthorizedOperationException when admin tries to change own role")
    void updateRole_ShouldThrowUnauthorizedOperationException_WhenAdminTriesToChangeOwnRole() {
      UUID adminId = adminUser.getId();
      RoleUpdateDto dto = new RoleUpdateDto(Role.CUSTOMER);
      when(auth.getDetails()).thenReturn(adminId);
      assertThrows(ForbiddenOperationException.class,
          () -> adminService.updateRole(adminId, dto, auth));
      verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("GetById should throw UserNotFoundException when user not found")
    void getById_ShouldThrowUserNotFoundException_WhenNotFound() {
      UUID fakeId = UUID.randomUUID();
      when(userRepository.findById(fakeId)).thenReturn(Optional.empty());
      assertThrows(UserNotFoundException.class, () -> adminService.getEntityById(fakeId));
    }
  }
}
