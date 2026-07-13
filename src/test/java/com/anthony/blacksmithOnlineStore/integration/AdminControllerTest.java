package com.anthony.blacksmithOnlineStore.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.controller.dto.admin.RoleUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserCreateDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.Role;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@Tag("integration")
@DisplayName("Integration test for admin controller")
public class AdminControllerTest extends TestBase {
  private String adminToken;
  private User admin;
  private User user;

  @BeforeEach
  void setUp() {
    adminToken = performLogin(adminLogin);
    admin = userRepository.findByUsername(adminLogin.username())
        .orElseThrow(() -> new IllegalStateException("Admin not found in test DB"));
    user = userRepository.findByUsername(userLogin.username())
        .orElseThrow(() -> new IllegalStateException("User not found in test DB"));
  }

  @Nested
  @DisplayName("Happy Path")
  class AdminControllerHappyPath {

    @Test
    @DisplayName("Admin can update an user role successfully")
    void updateRole_adminCanUpdateAnUserRoleSuccessfully() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(new RoleUpdateDto(Role.ADMIN));
      mockMvc.perform(patch("/admin/users/{id}/role", user.getId())
      .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isNoContent());
      assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    @DisplayName("Admin can get a user by id successfully")
    void getById_canGetAUserWithCorrectIdSuccessfully() throws Exception {
      mockMvc.perform(get("/admin/users?username={username}", user.getUsername())
              .header("Authorization", adminToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.role").value(user.getRole().name()))
          .andExpect(jsonPath("$.birthDate").value(user.getBirthDate().toString()))
          .andExpect(jsonPath("$.username").value(user.getUsername()))
          .andExpect(jsonPath("$.password").doesNotExist());
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class AdminControllerExceptionPath {

    @Test
    @DisplayName("Update role throws ForbiddenOperationException when admin tries update own role")
    void updateRole_throwsForbiddenOperationException_whenAdminTriesUpdateOwnRole()
        throws Exception {
      String valueAsString = objectMapper.writeValueAsString(new RoleUpdateDto(Role.CUSTOMER));
      mockMvc.perform(patch("/admin/users/{id}/role", admin.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update role is inaccessible to non-admin users")
    void updateRole_isUnaccessible_toNonAdminUsers()
        throws Exception {
      String userToken = performLogin(userLogin);
      String valueAsString = objectMapper.writeValueAsString(new RoleUpdateDto(Role.ADMIN));
      mockMvc.perform(patch("/admin/users/{id}/role", user.getId())
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get user by id returns 404 when username not exists")
    void register_shouldReturn400_whenUsernameIsInvalid() throws Exception {
      mockMvc.perform(get("/admin/users?username=unexistentUserName")
          .header("Authorization", adminToken))
          .andExpect(status().isNotFound())
          .andDo(print());
    }
  }
}
