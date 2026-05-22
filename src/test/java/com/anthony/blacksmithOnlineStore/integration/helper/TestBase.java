package com.anthony.blacksmithOnlineStore.integration.helper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.anthony.blacksmithOnlineStore.controller.dto.login.LoginRequest;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
public class TestBase {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  protected UserRepository userRepository;
  @Autowired
  PasswordEncoder passwordEncoder;
  protected final String AUTH_LOGIN_URL = "/auth/login";
  protected LoginRequest userLogin = new LoginRequest("user", "123456");
  protected LoginRequest adminLogin = new LoginRequest("admin", "loginAdmin");

  public String performLogin(LoginRequest loginRequest) {
    try {
      String valueAsString = objectMapper.writeValueAsString(loginRequest);
      MvcResult mvcResult = mockMvc.perform(post(AUTH_LOGIN_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andDo(print())
          .andReturn();
      String contentAsString = mvcResult.getResponse().getContentAsString();
      JSONObject json = new JSONObject(contentAsString);
      return "Bearer " + json.getString("token");
    } catch (Exception e) {
      throw new RuntimeException("Fail in perform login on test " + loginRequest.username(), e);
    }
  }

  public User performSaveUser(User entity) {
    entity.setId(null);
    entity.setPassword(passwordEncoder.encode(entity.getPassword()));
    return userRepository.save(entity);
  }
}
