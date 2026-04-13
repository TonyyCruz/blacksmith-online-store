package com.anthony.blacksmithOnlineStore.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.controler.dto.blacksmith.BlacksmithRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockBlacksmith;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockItem;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@DisplayName("Integration test for Item controller")
public class ItemControllerTest extends TestBase {

  @Autowired
  private ItemRepository itemRepository;
  private final String item_BASE_URL = "/items";
  private Item item;

  @BeforeEach
  void setUp() {
    item = itemRepository.findById(1L)
        .orElseThrow(() -> new IllegalStateException("Item not found in test DB"));
  }

  @Nested
  @Transactional
  @DisplayName("Happy Path")
  class ItemControllerHappyPath {
    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() throws Exception {
      adminToken = performLogin(adminLogin);
      userToken = performLogin(userLogin);
    }

    @Test
    @DisplayName("Can create a new Item successfully")
    void createItem_canCreateItemSuccessfully() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(MockItem.itemRequestDto());
      var a = mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.name").value(MockItem.itemRequestDto().name()))
          .andExpect(jsonPath("$.material").value(MockItem.itemRequestDto().material().toString()))
          .andExpect(jsonPath("$.baseDamage").value(MockItem.itemRequestDto().baseDamage()))
          .andExpect(jsonPath("$.baseDefense").value(MockItem.itemRequestDto().baseDefense()))
          .andExpect(jsonPath("$.basePrice").value(MockItem.itemRequestDto().basePrice().doubleValue()))
          .andExpect(jsonPath("$.finalPrice").value(MockItem.itemRequestDto().finalPrice().doubleValue()))
          .andExpect(jsonPath("$.description").value(MockItem.itemRequestDto().description()))
          .andExpect(jsonPath("$.weight").value(MockItem.itemRequestDto().weight().doubleValue()))
          .andExpect(jsonPath("$.stock").value(MockItem.itemRequestDto().stock()))
          .andExpect(jsonPath("$.type").value(MockItem.itemRequestDto().type().toString()))
          .andExpect(jsonPath("$.rarity").value(MockItem.itemRequestDto().rarity().toString()))
          .andExpect(jsonPath("$.active").value(MockItem.itemRequestDto().active()))
          .andExpect(jsonPath("$.craftedBy.id").value(MockItem.itemRequestDto().blacksmithId()));
      a.getClass();
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class ItemControllerExceptionPath {
    private String userToken;

    @BeforeEach
    void setUp() throws Exception {
      userToken = performLogin(userLogin);
    }


  }
}
