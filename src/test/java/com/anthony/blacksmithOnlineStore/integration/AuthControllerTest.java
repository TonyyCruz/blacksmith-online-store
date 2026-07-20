package com.anthony.blacksmithOnlineStore.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.anthony.blacksmithOnlineStore.controller.dto.login.LoginRequest;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserCreateDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.Role;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jayway.jsonpath.JsonPath;

@Tag("integration")
@DisplayName("Integration test for auth controller")
class AuthControllerTest extends TestBase {
  private final static String AUTH_REGISTER_URL = "/auth/register";
  private User user;

  @BeforeEach
  void setUp() {
    user = userRepository.findByUsername(userLogin.username())
        .orElseThrow(() -> new IllegalStateException("User not found in test DB"));
  }

  @Nested
  @DisplayName("Happy Path")
  class AuthControllerHappyPath {

    @Test
    @DisplayName("Register creates a new userWithId successfully")
    void register_createsNewUserSuccessfully() throws Exception {
      UserCreateDto dto = MockUser.userCreateDto();
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(post(AUTH_REGISTER_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.username").value(dto.username()))
          .andExpect(jsonPath("$.role").value(Role.CUSTOMER.name()))
          .andExpect(jsonPath("$.birthDate").value(dto.birthDate().toString()))
          .andExpect(jsonPath("$.password").doesNotExist())
          .andDo(print())
          .andReturn();
    }

    @Test
    @DisplayName("Login returns valid token when credentials are correct")
    void login_returnsValidToken_whenCredentialsAreCorrect() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(userLogin);
      MvcResult result = mockMvc.perform(post(AUTH_LOGIN_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isOk())
          .andReturn();
      String json = result.getResponse().getContentAsString();
      String token = JsonPath.read(json, "$.token");
      assertNotNull(token, "Token should not be null");
      DecodedJWT decoded = JWT.decode(token);
      assertEquals(user.getUsername(), decoded.getSubject());
      assertEquals(user.getId().toString(), decoded.getClaim("id").asString());
      assertEquals(user.getRole().name(), decoded.getClaim("role").asString());
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class AuthControllerExceptionPath {

    @Test
    @DisplayName("Login returns 401 when username not matching any userWithId")
    void login_shouldReturn401_whenUsernameNotMatchAnyUser() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(
          new LoginRequest("invalid", userLogin.password()));
      mockMvc.perform(post(AUTH_LOGIN_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isUnauthorized())
          .andDo(print());
    }

    @Test
    @DisplayName("Login returns 401 when password not matching")
    void login_shouldReturn401_whenPasswordNotMatching() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(
          new LoginRequest(userLogin.username(), "wrongPassword"));
      mockMvc.perform(post(AUTH_LOGIN_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isUnauthorized())
          .andDo(print());
    }

    @Test
    @DisplayName("Register returns 400 when username already exists")
    void register_shouldReturn400_whenUsernameAlreadyExists() throws Exception {
      UserCreateDto dto = MockUser.userCreateDto();
      dto = new UserCreateDto(
          user.getUsername(),
          dto.password(),
          dto.birthDate()
      );
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(post(AUTH_REGISTER_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isBadRequest())
          .andDo(print());
    }

    @Test
    @DisplayName("Register returns 400 when username is invalid")
    void register_shouldReturn400_whenUsernameIsInvalid() throws Exception {
      String[] wrongUsernames = {"", "   ", null};
      for (String username : wrongUsernames) {
        UserCreateDto dto = new UserCreateDto(
            username,
            MockUser.userCreateDto().password(),
            MockUser.userCreateDto().birthDate()
        );
        String valueAsString = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post(AUTH_REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(status().isBadRequest())
            .andDo(print());
      }
    }

    @Test
    @DisplayName("Register returns 400 when birthdate is invalid")
    void register_shouldReturn400_whenBirthdateIsInvalid() throws Exception {
      LocalDate[] invalidDates = {
          null,
          LocalDate.now().plusYears(10),
          LocalDate.now().minusYears(17),
          LocalDate.now()
      };
      for (LocalDate date : invalidDates) {
        UserCreateDto dto = new UserCreateDto(
            MockUser.userCreateDto().username(),
            MockUser.userCreateDto().password(),
            date
        );
        String valueAsString = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post(AUTH_REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(status().isBadRequest())
            .andDo(print());
      }
    }

    @Test
    @DisplayName("Register returns 400 when password is invalid")
    void register_shouldReturn400_whenPasswordIsInvalid() throws Exception {
      String[] wrongPass = {"short1#", "alllowercase1!", "ALLUPPERCASE1!", "NoNumbers!",
          "NoSpecialChar1"};
      for (String pwd : wrongPass) {
        UserCreateDto dto = new UserCreateDto(
            MockUser.userCreateDto().username(),
            pwd,
            MockUser.userCreateDto().birthDate()
        );
        String valueAsString = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post(AUTH_REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(status().isBadRequest())
            .andDo(print());
      }
    }
  }
}
