package com.anthony.blacksmithOnlineStore.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockItem;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import jakarta.transaction.Transactional;
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
      mockMvc.perform(post(item_BASE_URL + "/create")
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.name").value(MockItem.itemRequestDto().name()))
          .andExpect(jsonPath("$.material").value(MockItem.itemRequestDto().material().toString()))
          .andExpect(jsonPath("$.baseDamage").value(MockItem.itemRequestDto().baseDamage()))
          .andExpect(jsonPath("$.baseDefense").value(MockItem.itemRequestDto().baseDefense()))
          .andExpect(
              jsonPath("$.basePrice").value(MockItem.itemRequestDto().basePrice().doubleValue()))
          .andExpect(
              jsonPath("$.finalPrice").value(MockItem.itemRequestDto().finalPrice().doubleValue()))
          .andExpect(jsonPath("$.description").value(MockItem.itemRequestDto().description()))
          .andExpect(jsonPath("$.weight").value(MockItem.itemRequestDto().weight().doubleValue()))
          .andExpect(jsonPath("$.stock").value(MockItem.itemRequestDto().stock()))
          .andExpect(jsonPath("$.type").value(MockItem.itemRequestDto().type().toString()))
          .andExpect(jsonPath("$.rarity").value(MockItem.itemRequestDto().rarity().toString()))
          .andExpect(jsonPath("$.active").value(MockItem.itemRequestDto().active()))
          .andExpect(jsonPath("$.craftedBy.id").value(MockItem.itemRequestDto().blacksmithId()));
    }

    @Test
    @DisplayName("Can update an existing Item successfully")
    void updateItem_canUpdateItemSuccessfully() throws Exception {
      ItemRequestDto itemUpdate = MockItem.itemRequestDto();
      String valueAsString = objectMapper.writeValueAsString(itemUpdate);
      mockMvc.perform(put(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value(itemUpdate.name()))
          .andExpect(jsonPath("$.material").value(itemUpdate.material().toString()))
          .andExpect(jsonPath("$.baseDamage").value(itemUpdate.baseDamage()))
          .andExpect(jsonPath("$.baseDefense").value(itemUpdate.baseDefense()))
          .andExpect(jsonPath("$.basePrice").value(itemUpdate.basePrice().doubleValue()))
          .andExpect(jsonPath("$.finalPrice").value(itemUpdate.finalPrice().doubleValue()))
          .andExpect(jsonPath("$.description").value(itemUpdate.description()))
          .andExpect(jsonPath("$.weight").value(itemUpdate.weight().doubleValue()))
          .andExpect(jsonPath("$.stock").value(itemUpdate.stock()))
          .andExpect(jsonPath("$.type").value(itemUpdate.type().toString()))
          .andExpect(jsonPath("$.rarity").value(itemUpdate.rarity().toString()))
          .andExpect(jsonPath("$.active").value(itemUpdate.active()))
          .andExpect(jsonPath("$.craftedBy.id").value(itemUpdate.blacksmithId()));
    }

    @Test
    @DisplayName("Can do a partial update successfully")
    void patchUpdate_canDoPartialUpdateSuccessfully() throws Exception {
      ItemPatchUpdateDto itemUpdate = MockItem.itemPatchUpdateDto();
      String valueAsString = objectMapper.writeValueAsString(itemUpdate);
      mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isOk());

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
