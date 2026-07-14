package com.anthony.blacksmithOnlineStore.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.controller.dto.login.LoginRequest;
import com.anthony.blacksmithOnlineStore.controller.dto.user.PasswordUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserUpdateDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class UserControllerTest extends TestBase {
  private static final String USER_URL = "/users/me";
  private User user;
  private String userToken;

  @BeforeEach
  void setUp() {
    userToken = performLogin(userLogin);
    user = userRepository.findByUsername(userLogin.username())
        .orElseThrow(() -> new IllegalStateException("User not found in test DB"));
  }

  @Nested
  @DisplayName("Happy Path")
  class UserControllerHappyPath {

    @Test
    @DisplayName("Get Current User returns userWithId data successfully")
    void getCurrentUser_returnsUserDataSuccessfully() throws Exception {
      mockMvc.perform(get(USER_URL)
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.role").value(user.getRole().name()))
          .andExpect(jsonPath("$.birthDate").value(user.getBirthDate().toString()))
          .andExpect(jsonPath("$.username").value(user.getUsername()))
          .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @DisplayName("Update Current User updates userWithId data successfully")
    void updateCurrentUser_updatesUserDataSuccessfully() throws Exception {
      UserUpdateDto dto = MockUser.userUpdateDto();
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(put(USER_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(user.getId().toString()))
          .andExpect(jsonPath("$.username").value(dto.username()))
          .andExpect(jsonPath("$.birthDate").value(dto.birthDate().toString()))
          .andExpect(jsonPath("$.password").doesNotExist())
          .andDo(print());
    }

    @Test
    @DisplayName("Current User can update their data using the same username")
    void updateCurrentUser_updatesTheirDataUsingSameUsername() throws Exception {
      UserUpdateDto dto = new UserUpdateDto(user.getUsername(),
          LocalDate.parse("1982-01-01"));
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(put(USER_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(user.getId().toString()))
          .andExpect(jsonPath("$.username").value(dto.username()))
          .andExpect(jsonPath("$.birthDate").value(dto.birthDate().toString()))
          .andExpect(jsonPath("$.password").doesNotExist())
          .andDo(print());
    }

    @Test
    @DisplayName("Update password can be done successfully with correct current password")
    void updatePassword_canBeDoneSuccessfully_withCorrectCurrentPassword() throws Exception {
      String newPassword = "NewP@ssw0rd!";
      PasswordUpdateDto dto = new PasswordUpdateDto(userLogin.password(), newPassword);
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(put(USER_URL + "/password")
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isNoContent());
      LoginRequest loginRequest = new LoginRequest(userLogin.username(), newPassword);
      String loginAsString = objectMapper.writeValueAsString(loginRequest);
      mockMvc.perform(post(AUTH_LOGIN_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(loginAsString))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete Current User deletes userWithId successfully")
    void deleteCurrentUser_deletesUserSuccessfully() throws Exception {
      mockMvc.perform(delete(USER_URL)
              .header("Authorization", userToken))
          .andExpect(status().isNoContent())
          .andDo(print());
      String loginAsString = objectMapper.writeValueAsString(userLogin);
      mockMvc.perform(post(AUTH_LOGIN_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(loginAsString))
          .andExpect(status().isUnauthorized())
          .andDo(print());
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class UserControllerExceptionPath {

    @Test
    @DisplayName("Get Current User returns 403 when no auth token is provided")
    void getCurrentUser_returns403_whenNoAuthTokenIsProvided() throws Exception {
      mockMvc.perform(get(USER_URL))
          .andExpect(status().isForbidden())
          .andDo(print());
    }

    @Test
    @DisplayName("Update Current User returns 403 when no auth token is provided")
    void updateCurrentUser_returns403_whenNoAuthTokenIsProvided() throws Exception {
      UserUpdateDto dto = MockUser.userUpdateDto();
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(put(USER_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden())
          .andDo(print());
    }

    @Test
    @DisplayName("Update Password returns 403 when no auth token is provided")
    void updatePassword_returns403_whenNoAuthTokenIsProvided() throws Exception {
      PasswordUpdateDto dto = new PasswordUpdateDto("oldPass", "newPass");
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(put(USER_URL + "/password")
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden())
          .andDo(print());
    }

    @Test
    @DisplayName("Delete Current User returns 403 when no auth token is provided")
    void deleteCurrentUser_returns403_whenNoAuthTokenIsProvided() throws Exception {
      mockMvc.perform(delete(USER_URL))
          .andExpect(status().isForbidden())
          .andDo(print());
    }

    @Test
    @DisplayName("Update Password returns 400 when current password is incorrect")
    void updatePassword_returns400_whenCurrentPasswordIsIncorrect() throws Exception {
      PasswordUpdateDto dto = new PasswordUpdateDto("WrongP@ssw0rd!", "NewP@ssw0rd!");
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(put(USER_URL + "/password")
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isBadRequest())
          .andDo(print());
    }

    @Test
    @DisplayName("Update Current User returns 400 when username is already taken")
    void updateCurrentUser_returns400_whenUsernameIsAlreadyTaken() throws Exception {
      User anotherUser = performSaveUser(MockUser.userWithId());
      UserUpdateDto dto = new UserUpdateDto(anotherUser.getUsername(),
          LocalDate.parse("1900-01-01"));
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(put(USER_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isBadRequest())
          .andDo(print());
    }

    @Test
    @DisplayName("Update Current User returns 400 when username is invalid")
    void updateCurrentUser_returns400_whenUsernameIsInvalid() throws Exception {
      String[] wrongUsername = {"", "   ", null};
      for (String usrName : wrongUsername) {
        UserUpdateDto dto = new UserUpdateDto(usrName, MockUser.userWithId().getBirthDate());
        String valueAsString = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put(USER_URL)
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(status().isBadRequest())
            .andDo(print());
      }
    }

    @Test
    @DisplayName("Update Current User returns 400 when birthdate is invalid")
    void updateCurrentUser_returns400_whenBirthdateIsInvalid() throws Exception {
      LocalDate[] invalidDates = {
          null,
          LocalDate.now().plusYears(10),
          LocalDate.now().minusYears(17),
          LocalDate.now()
      };
      for (LocalDate date : invalidDates) {
        UserUpdateDto dto = new UserUpdateDto(MockUser.userWithId().getUsername(), date);
        mockMvc.perform(put(USER_URL)
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andDo(print());
      }
    }

    @Test
    @DisplayName("Update Password returns 400 when it have not meet complexity requirements")
    void updatePassword_returns400_whenInvalidDataIsProvided() throws Exception {
      String[] wrongPass = {"short1#", "alllowercase1!", "ALLUPPERCASE1!", "NoNumbers!",
          "NoSpecialChar1"};
      for (String pwd : wrongPass) {
        PasswordUpdateDto dto = new PasswordUpdateDto(
            userLogin.password(),
            pwd
        );
        String valueAsString = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put(USER_URL)
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(status().isBadRequest())
            .andDo(print());
      }
    }

    @Test
    @DisplayName("Delete Current User returns 401 when auth token is invalid")
    void deleteCurrentUser_returns401_whenAuthTokenIsInvalid() throws Exception {
      mockMvc.perform(delete(USER_URL)
              .header("Authorization", "Bearer InvalidToken"))
          .andExpect(status().isUnauthorized())
          .andDo(print());
    }
  }
}
