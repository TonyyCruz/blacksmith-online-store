package com.anthony.blacksmithOnlineStore.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.controller.dto.admin.RoleUpdateDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.enums.Role;
import jakarta.transaction.Transactional;
import java.util.UUID;
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
  void setUp() throws Exception {
    adminToken = performLogin(adminLogin);
    admin = userRepository.findByUsername(adminLogin.username())
        .orElseThrow(() -> new IllegalStateException("Admin not found in test DB"));
    user = userRepository.findByUsername(userLogin.username())
        .orElseThrow(() -> new IllegalStateException("User not found in test DB"));
  }

  @Nested
  @Transactional
  @DisplayName("Happy Path")
  class AdminControllerHappyPath {

    @Test
    @DisplayName("Admin can update an user role successfully")
    void updateRole_adminCanUpdateAnUserRoleSuccessfully() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(new RoleUpdateDto(Role.ADMIN));
      mockMvc.perform(patch(roleUpdateUrl(user.getId()))
      .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isNoContent());
      assertEquals(Role.ADMIN, user.getRole());
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
      mockMvc.perform(patch(roleUpdateUrl(admin.getId()))
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
      mockMvc.perform(patch(roleUpdateUrl(admin.getId()))
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden());
    }
  }

  private String roleUpdateUrl(UUID id) {
    return "/admin/users/{orderId}/role".replace("{orderId}", id.toString());
  }
}
