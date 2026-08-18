package com.anthony.blacksmithOnlineStore.helper.mocks;

import com.anthony.blacksmithOnlineStore.controller.dto.user.PasswordUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.user.UserUpdateDto;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.Role;
import java.time.LocalDate;
import java.util.UUID;

public class MockUser {

  public static User user() {
    User user = new User();
    user.setUsername("user_one");
    user.setPassword("Testp4ssw0rd@");
    user.setRole(Role.CUSTOMER);
    user.setBirthDate(LocalDate.of(1995, 5, 15));
    return user;
  }

  public static User user(UUID id) {
    User user = user();
    user.setId(id);
    return user;
  }

  public static User admin() {
    User admin = new User();
    admin.setId(UUID.randomUUID());
    admin.setUsername("super_admin");
    admin.setPassword("loginAdmin01#");
    admin.setRole(Role.ADMIN);
    admin.setBirthDate(LocalDate.of(1988, 1, 10));
    return admin;
  }

  // ========== DTOs ==========

  public static UserCreateDto userCreateDto() {
    return new UserCreateDto("user_one", "UserPass123@", LocalDate.of(1995, 5, 15));
  }

  public static UserUpdateDto userUpdateDto() {
    return new UserUpdateDto("user_two", LocalDate.of(1996, 6, 20));
  }

  public static PasswordUpdateDto passwordUpdateDto() {
    return new PasswordUpdateDto("UserPass123@", "UserNewPass123*");
  }

}
