package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.anthony.blacksmithOnlineStore.controller.dto.user.PasswordUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserUpdateDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidCredentialsException;
import com.anthony.blacksmithOnlineStore.exceptions.UsernameAlreadyExistsException;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.repository.UserRepository;
import com.anthony.blacksmithOnlineStore.enums.Role;
import com.anthony.blacksmithOnlineStore.security.utils.AuthenticatedUserService;
import com.anthony.blacksmithOnlineStore.service.UserService;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@Tag("unit")
@DisplayName("Unit test for UserService")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private AuthenticatedUserService authUser;
  @InjectMocks private UserService userService;
  private User targetUser;

  @BeforeEach
  void setup() {
    targetUser = MockUser.user();
  }

  @Nested
  @DisplayName("Happy Path")
  class UserServiceHappyPath {

    @Test
    @DisplayName("Create should encode password and save user when username is available")
    void create_shouldEncodePasswordAndSaveUser_whenUsernameIsAvailable() {
      UserCreateDto dto = MockUser.userCreateDto();

      when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
      when(passwordEncoder.encode(any(String.class))).thenReturn("encoded");
      when(userRepository.save(any(User.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      UserDto created = userService.create(dto);

      assertEquals(Role.CUSTOMER.name(), created.role(), "The default role must be CUSTOMER");
      verify(passwordEncoder).encode(dto.password());
      assertEquals(dto.username(), created.username(), "The username must be maintained");
      assertEquals(dto.birthDate().toString(), created.birthDate(), "The birth date must be maintained");
      verify(userRepository, times(1)).save(any(User.class));
      verify(userRepository, times(1)).existsByUsername(dto.username());
    }

    @Test
    @DisplayName("UpdateUser should update username and birthdate when username is available")
    void update_CanUpdateUsernameAndBirthdate_whenUsernameIsAvailable() {
      UserUpdateDto dto = MockUser.userUpdateDto();

      when(authUser.getName()).thenReturn(targetUser.getUsername());
      when(authUser.getAuthenticatedId()).thenReturn(targetUser.getId());
      when(userRepository.existsByUsername(dto.username())).thenReturn(false);
      when(userRepository.getReferenceById(any(UUID.class))).thenReturn(targetUser);
      when(userRepository.save(any(User.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      UserDto updated = userService.updateUser(dto);

      assertEquals(Role.CUSTOMER.name(), updated.role(), "The Role should not be changed");
      assertEquals(dto.username(), updated.username(), "The username must be updated");
      assertEquals(dto.birthDate().toString(), updated.birthDate(), "The birth date must be updated");
      verify(userRepository, times(1)).save(any(User.class));
      verify(userRepository, times(1)).existsByUsername(dto.username());
    }

    @Test
    @DisplayName("UpdateUser should update birthdate when username is the same")
    void update_CanUpdateBirthdate_whenUsernameIsTheSelf() {
      UserUpdateDto dto = new UserUpdateDto(targetUser.getUsername(), LocalDate.of(2000, 1, 1));

      when(authUser.getName()).thenReturn(targetUser.getUsername());
      when(authUser.getAuthenticatedId()).thenReturn(targetUser.getId());
      when(userRepository.getReferenceById(any(UUID.class))).thenReturn(targetUser);
      when(userRepository.save(any(User.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      UserDto updated = userService.updateUser(dto);

      assertEquals(Role.CUSTOMER.name(), updated.role(), "The Role should not be changed");
      assertEquals(dto.username(), updated.username(), "The username must be updated");
      assertEquals(dto.birthDate().toString(), updated.birthDate(), "The birth date must be updated");
      verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("UpdatePassword should update password when current password is correct")
    void updatePassword_shouldUpdatePassword_whenCurrentPasswordIsCorrect() {
      targetUser.setPassword("encoded");
      PasswordUpdateDto dto = MockUser.passwordUpdateDto();

      when(authUser.getAuthenticatedId()).thenReturn(targetUser.getId());
      when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(targetUser));
      when(passwordEncoder.matches(dto.currentPassword(), targetUser.getPassword())).thenReturn(true);
      when(passwordEncoder.encode(dto.newPassword())).thenReturn("newEncoded");
      when(userRepository.save(any(User.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      userService.updatePassword(dto);
      assertEquals("newEncoded", targetUser.getPassword());
      verify(passwordEncoder).matches(dto.currentPassword(), "encoded");
      verify(passwordEncoder).encode(dto.newPassword());
      verify(userRepository, times(1)).findById(targetUser.getId());
      verify(userRepository, times(1)).save(any(User.class));
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class UserServiceExceptionPath {

    @Test
    @DisplayName("Create should throw UsernameAlreadyExistsException when username is not available")
    void create_shouldThrowUsernameAlreadyExistsException_whenUsernameIsNotAvailable() {
      UserCreateDto dto = MockUser.userCreateDto();

      when(userRepository.existsByUsername(dto.username())).thenReturn(true);

      assertThrows(UsernameAlreadyExistsException.class,
          () -> userService.create(dto));
      verify(userRepository, times(1)).existsByUsername(dto.username());
    }

    @Test
    @DisplayName("UpdateUser should throw UsernameAlreadyExistsException when new username is not available")
    void updateUser_shouldThrowUsernameAlreadyExistsException_whenNewUsernameIsNotAvailable() {
      UserUpdateDto dto = MockUser.userUpdateDto();

      when(userRepository.existsByUsername(dto.username())).thenReturn(true);
      when(authUser.getName()).thenReturn("differentUsername");

      assertThrows(UsernameAlreadyExistsException.class,
          () -> userService.updateUser(dto));
      verify(userRepository, times(1)).existsByUsername(dto.username());
      }

    @Test
    @DisplayName("UpdatePassword should throw InvalidCredentialsException when current password is wrong")
    void updatePassword_shouldThrowInvalidCredentials_whenCurrentPasswordIsWrong() {
      targetUser.setPassword("encoded");

      when(authUser.getAuthenticatedId()).thenReturn(targetUser.getId());
      when(userRepository.findById(any(UUID.class)))
          .thenReturn(Optional.of(targetUser));
      when(passwordEncoder.matches("wrong", "encoded"))
          .thenReturn(false);

      PasswordUpdateDto dto = new PasswordUpdateDto("wrong", "newPass");

      assertThrows(InvalidCredentialsException.class,
          () -> userService.updatePassword(dto));
      verify(userRepository, times(1))
          .findById(targetUser.getId());
      verify(passwordEncoder, times(1))
          .matches("wrong", "encoded");
    }
  }
}
