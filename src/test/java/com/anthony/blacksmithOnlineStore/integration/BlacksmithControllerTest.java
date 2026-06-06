package com.anthony.blacksmithOnlineStore.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.controller.dto.blacksmith.BlacksmithRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockBlacksmith;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.BlacksmithRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Tag("integration")
@DisplayName("Integration test for Blacksmith controller")
public class BlacksmithControllerTest extends TestBase {
  @Autowired
  private BlacksmithRepository blacksmithRepository;
  private final String BLACKSMITH_BASE_URL = "/blacksmiths";
  private Blacksmith blacksmith;
  private String userToken;

  @BeforeEach
  void setUp() {
    blacksmith = blacksmithRepository.findById(1L)
        .orElseThrow(() -> new IllegalStateException("Blacksmith not found in test DB"));
    userToken = performLogin(userLogin);
  }

  @Nested
  @Transactional
  @DisplayName("Happy Path")
  class BlacksmithControllerHappyPath {
    private String adminToken;

    @BeforeEach
    void setUp() {
      adminToken = performLogin(adminLogin);
    }

    @Test
    @Transactional
    @DisplayName("Can create blacksmith successfully")
    void createBlacksmith_canCreateBlacksmithSuccessfully() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(MockBlacksmith.requestDto());
      mockMvc.perform(post(BLACKSMITH_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.name").value(MockBlacksmith.requestDto().name()))
          .andExpect(jsonPath("$.description").value(MockBlacksmith.requestDto().description()))
          .andExpect(jsonPath("$.ratingCount").value(0))
          .andExpect(jsonPath("$.ratingAverage").value(Matchers.nullValue()));
    }

    @Test
    @Transactional
    @DisplayName("Can update blacksmith successfully")
    void updateBlacksmith_canUpdateBlacksmithSuccessfully() throws Exception {
      var updateDto = new BlacksmithRequestDto("Updated Name", "Updated Description");
      String valueAsString = objectMapper.writeValueAsString(updateDto);
      String updateUrl = BLACKSMITH_BASE_URL + "/" + blacksmith.getId();
      mockMvc.perform(put(updateUrl)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value(updateDto.name()))
          .andExpect(jsonPath("$.description").value(updateDto.description()))
          .andExpect(jsonPath("$.ratingCount").value(blacksmith.getRatingCount()))
          .andExpect(jsonPath("$.ratingAverage").value(blacksmith.getRatingAverage()));
    }

    @Test
    @DisplayName("Can get blacksmith by ID successfully")
    void findById_canGetBlacksmithByIdSuccessfully() throws Exception {
      String findByIdUrl = BLACKSMITH_BASE_URL + "/" + blacksmith.getId();
      mockMvc.perform(get(findByIdUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value(blacksmith.getName()))
          .andExpect(jsonPath("$.description").value(blacksmith.getDescription()))
          .andExpect(jsonPath("$.ratingCount").value(blacksmith.getRatingCount()))
          .andExpect(jsonPath("$.ratingAverage").value(blacksmith.getRatingAverage()));
    }

    @Test
    @DisplayName("Can get all blacksmiths successfully")
    void findAll_canGetAllBlacksmithsSuccessfully() throws Exception {
      mockMvc.perform(get(BLACKSMITH_BASE_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    @DisplayName("Can search blacksmiths by name successfully")
    void findByName_canSearchBlacksmithsByNameSuccessfully() throws Exception {
      String searchName = blacksmith.getName().substring(0, 3);
      String searchUrl = BLACKSMITH_BASE_URL + "/search?name=" + searchName;
      mockMvc.perform(get(searchUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content.length()").value(1));
      searchUrl = BLACKSMITH_BASE_URL + "/search?name=" + "e";
      mockMvc.perform(get(searchUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content.length()").value(2));
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class BlacksmithControllerExceptionPath {

    @Test
    @DisplayName("Cannot create blacksmith with user role")
    void createBlacksmith_cannotCreateBlacksmithWithUserRole() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(MockBlacksmith.requestDto());
      mockMvc.perform(post(BLACKSMITH_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Cannot update blacksmith with user role")
    void updateBlacksmith_cannotUpdateBlacksmithWithUserRole() throws Exception {
      var updateDto = new BlacksmithRequestDto("Updated Name", "Updated Description");
      String valueAsString = objectMapper.writeValueAsString(updateDto);
      String updateUrl = BLACKSMITH_BASE_URL + "/" + blacksmith.getId();
      mockMvc.perform(put(updateUrl)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Create blacksmith throws exception when name is invalid")
    void createBlacksmith_throwsExceptionWhenNameIsInvalid() throws Exception {
      String[] invalidNames = {"", "  ", "A", null};
      for (String invalidName : invalidNames) {
        String valueAsString = objectMapper.writeValueAsString(
            MockBlacksmith.requestDto(invalidName, "Valid Description"));
        String adminToken = performLogin(adminLogin);
        mockMvc.perform(post(BLACKSMITH_BASE_URL)
                .header("Authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(status().isBadRequest());
      }
    }

    @Test
    @DisplayName("Create blacksmith throws exception when description is invalid")
    void createBlacksmith_throwsExceptionWhenDescriptionIsInvalid() throws Exception {
      String[] invalidDescriptions = {"", "  ", "Too short", null};
      for (String invalidDescription : invalidDescriptions) {
        String valueAsString = objectMapper.writeValueAsString(
            MockBlacksmith.requestDto("Valid Name", invalidDescription));
        String adminToken = performLogin(adminLogin);
        mockMvc.perform(post(BLACKSMITH_BASE_URL)
                .header("Authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
            .andExpect(status().isBadRequest());
      }
    }
  }
}
