package com.anthony.blacksmithOnlineStore.integration;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemFilterDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidItemDataException;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockBlacksmith;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockItem;
import com.anthony.blacksmithOnlineStore.integration.helper.QueryHelper;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.BlacksmithRepository;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;

@Tag("integration")
@DisplayName("Integration test for Item controller")
public class ItemControllerTest extends TestBase {
  @Autowired
  private ItemRepository itemRepository;
  @Autowired
  private BlacksmithRepository blacksmithRepository;
  private final String item_BASE_URL = "/items";
  private Item item;
  private String adminToken;
  private String userToken;

  @BeforeEach
  void setUp() {
    item = saveItem(Item.builder()
        .material(Material.IRON)
        .baseDamage(250)
        .baseDefense(10)
        .name("Test Dagger")
        .basePrice(BigDecimal.valueOf(355.99))
        .finalPrice(BigDecimal.valueOf(350.00))
        .hasDiscount(true)
        .description("Dagger for tests")
        .weight(8.2d)
        .stock(102)
        .type(Type.DAGGER)
        .rarity(Rarity.TRANSCENDENT)
        .craftedBy(MockBlacksmith.blacksmith())
        .blacksmithIdSnapshot(MockBlacksmith.blacksmith().getId())
        .blacksmithNameSnapshot(MockBlacksmith.blacksmith().getName())
        .ratingCount(0)
        .active(true)
        .build());
    adminToken = performLogin(adminLogin);
    userToken = performLogin(userLogin);
  }

  @Nested
  @DisplayName("Happy Path")
  class ItemControllerHappyPath {

    @Test
    @DisplayName("Can create a new Item successfully")
    void createItem_canCreateItemSuccessfully() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto();
      Blacksmith blacksmith = findBlacksmithById(dto.blacksmithId());
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.name").value(dto.name()))
          .andExpect(jsonPath("$.material").value(dto.material().toString()))
          .andExpect(jsonPath("$.baseDamage").value(dto.baseDamage()))
          .andExpect(jsonPath("$.baseDefense").value(dto.baseDefense()))
          .andExpect(
              jsonPath("$.basePrice").value(dto.basePrice().doubleValue()))
          .andExpect(
              jsonPath("$.finalPrice").value(dto.finalPrice().doubleValue()))
          .andExpect(jsonPath("$.description").value(dto.description()))
          .andExpect(jsonPath("$.weight").value(dto.weight().doubleValue()))
          .andExpect(jsonPath("$.stock").value(dto.stock()))
          .andExpect(jsonPath("$.type").value(dto.type().toString()))
          .andExpect(jsonPath("$.rarity").value(dto.rarity().toString()))
          .andExpect(jsonPath("$.active").value(dto.active()))
          .andExpect(jsonPath("$.blacksmithId").value(blacksmith.getId()))
          .andExpect(jsonPath("$.blacksmithName").value(blacksmith.getName()));
    }

    @Test
    @DisplayName("Can update an existing Item successfully")
    void updateItem_canUpdateItemSuccessfully() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto();
      Blacksmith blacksmith = findBlacksmithById(dto.blacksmithId());
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(put(item_BASE_URL + "/{id}", item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value(dto.name()))
          .andExpect(jsonPath("$.material").value(dto.material().toString()))
          .andExpect(jsonPath("$.baseDamage").value(dto.baseDamage()))
          .andExpect(jsonPath("$.baseDefense").value(dto.baseDefense()))
          .andExpect(jsonPath("$.basePrice").value(dto.basePrice().doubleValue()))
          .andExpect(jsonPath("$.finalPrice").value(dto.finalPrice().doubleValue()))
          .andExpect(jsonPath("$.description").value(dto.description()))
          .andExpect(jsonPath("$.weight").value(dto.weight().doubleValue()))
          .andExpect(jsonPath("$.stock").value(dto.stock()))
          .andExpect(jsonPath("$.type").value(dto.type().toString()))
          .andExpect(jsonPath("$.rarity").value(dto.rarity().toString()))
          .andExpect(jsonPath("$.active").value(dto.active()))
          .andExpect(jsonPath("$.blacksmithId").value(blacksmith.getId()))
          .andExpect(jsonPath("$.blacksmithName").value(blacksmith.getName()));
    }

    @Test
    @DisplayName("Can update all fields witha PATH update successfully")
    void patchUpdate_canUpdateAllFieldsSuccessfully() throws Exception {
      ItemPatchUpdateDto itemUpdate = MockItem.itemPatchUpdateDto();
      Blacksmith blacksmith = findBlacksmithById(itemUpdate.blacksmithId());
      String valueAsString = objectMapper.writeValueAsString(itemUpdate);
      mockMvc.perform(patch(item_BASE_URL + "/{id}", item.getId())
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
          .andExpect(jsonPath("$.blacksmithId").value(blacksmith.getId()))
          .andExpect(jsonPath("$.blacksmithName").value(blacksmith.getName()));
    }

    @Test
    @DisplayName("Can do a partial update successfully")
    void patchUpdate_canDoPartialUpdateSuccessfully() throws Exception {
      ItemPatchUpdateDto itemUpdate = ItemPatchUpdateDto.builder()
          .name("Patched " + item.getName())
          .baseDamage(item.getBaseDamage() + 10)
          .baseDefense(item.getBaseDefense() + 5)
          .description("Patched " + item.getDescription())
          .type(item.getType())
          .rarity(Rarity.LEGENDARY)
          .active(!item.isActive())
          .blacksmithId(2L)
          .build();
      Blacksmith blacksmith = findBlacksmithById(itemUpdate.blacksmithId());
      String valueAsString = objectMapper.writeValueAsString(itemUpdate);
      mockMvc.perform(patch(item_BASE_URL + "/{id}", item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value(itemUpdate.name()))
          .andExpect(jsonPath("$.material").value(item.getMaterial().toString()))
          .andExpect(jsonPath("$.baseDamage").value(itemUpdate.baseDamage()))
          .andExpect(jsonPath("$.baseDefense").value(itemUpdate.baseDefense()))
          .andExpect(jsonPath("$.basePrice").value(item.getBasePrice().doubleValue()))
          .andExpect(jsonPath("$.finalPrice").value(item.getFinalPrice().doubleValue()))
          .andExpect(jsonPath("$.description").value(itemUpdate.description()))
          .andExpect(jsonPath("$.weight").value(item.getWeight().doubleValue()))
          .andExpect(jsonPath("$.stock").value(item.getStock()))
          .andExpect(jsonPath("$.type").value(itemUpdate.type().toString()))
          .andExpect(jsonPath("$.rarity").value(itemUpdate.rarity().toString()))
          .andExpect(jsonPath("$.active").value(itemUpdate.active()))
          .andExpect(jsonPath("$.blacksmithId").value(blacksmith.getId()))
          .andExpect(jsonPath("$.blacksmithName").value(blacksmith.getName()));

    }

    @Test
    @DisplayName("Can get an Item by id successfully")
    void getItemById_canGetItemSuccessfully() throws Exception {
      mockMvc.perform(get(item_BASE_URL + "/{id}", item.getId())
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value(item.getName()))
          .andExpect(jsonPath("$.material").value(item.getMaterial().toString()))
          .andExpect(jsonPath("$.baseDamage").value(item.getBaseDamage()))
          .andExpect(jsonPath("$.baseDefense").value(item.getBaseDefense()))
          .andExpect(jsonPath("$.basePrice").value(item.getBasePrice().doubleValue()))
          .andExpect(jsonPath("$.finalPrice").value(item.getFinalPrice().doubleValue()))
          .andExpect(jsonPath("$.description").value(item.getDescription()))
          .andExpect(jsonPath("$.weight").value(item.getWeight().doubleValue()))
          .andExpect(jsonPath("$.stock").value(item.getStock()))
          .andExpect(jsonPath("$.type").value(item.getType().toString()))
          .andExpect(jsonPath("$.rarity").value(item.getRarity().toString()))
          .andExpect(jsonPath("$.active").value(item.isActive()))
          .andExpect(jsonPath("$.blacksmithId").value(item.getCraftedBy().getId()));
    }

    @Test
    @DisplayName("Can get all active itens with user acount when filter is empty")
    void getAllActiveItems_canGetAllActiveItensSuccessfully_withUserAccount() throws Exception {
      long itemQuantity = itemRepository.findAll().stream().filter(Item::isActive).count();
      mockMvc.perform(get(item_BASE_URL)
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content").isNotEmpty())
          .andExpect(jsonPath("$.content.size()").value(itemQuantity))
          .andExpect(jsonPath("$.content[*].active").value(everyItem(is(true))))
          .andExpect(jsonPath("$.content[0].name").value(item.getName()))
          .andExpect(jsonPath("$.content[0].material")
              .value(item.getMaterial().toString()))
          .andExpect(jsonPath("$.content[0].baseDamage").value(item.getBaseDamage()))
          .andExpect(jsonPath("$.content[0].baseDefense").value(item.getBaseDefense()))
          .andExpect(jsonPath("$.content[0].basePrice")
              .value(item.getBasePrice().doubleValue()))
          .andExpect(jsonPath("$.content[0].finalPrice")
              .value(item.getFinalPrice().doubleValue()))
          .andExpect(jsonPath("$.content[0].description").value(item.getDescription()))
          .andExpect(jsonPath("$.content[0].weight")
              .value(item.getWeight().doubleValue()))
          .andExpect(jsonPath("$.content[0].stock").value(item.getStock()))
          .andExpect(jsonPath("$.content[0].type").value(item.getType().toString()))
          .andExpect(jsonPath("$.content[0].rarity").value(item.getRarity().toString()))
          .andExpect(jsonPath("$.content[0].active").value(item.isActive()))
          .andExpect(jsonPath("$.content[0].blacksmithId")
              .value(item.getCraftedBy().getId()));
    }

    @Test
    @DisplayName("Can return empty list when filter have no match")
    void filter_shouldReturnEmpty_whenNoMatch() throws Exception {
      mockMvc.perform(get(item_BASE_URL)
              .header("Authorization", adminToken)
              .param("name", "NONEXISTENT_ITEM_999999999"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @DisplayName("Can get all itens with admin acount and empty search")
    void getAllActiveItems_canGetAllFilteredItemsSuccessfully_withAdminAccount() throws Exception {
      int itemQuantity = itemRepository.findAll().size();
      mockMvc.perform(get(item_BASE_URL)
              .header("Authorization", adminToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content").isNotEmpty())
          .andExpect(jsonPath("$.content.size()").value(itemQuantity))
          .andExpect(jsonPath("$.content[*].active", hasItem(true)))
          .andExpect(jsonPath("$.content[*].active", hasItem(false)));
    }

    @Test
    @DisplayName("Can get all active itens that match filter acount")
    void getAllActiveItems_canGetAllFilteredMatchedItemsSuccessfully() throws Exception {
      ItemFilterDto filter = new ItemFilterDto("Sword of Valor", Material.STEEL, 50,
          51, 20, 21, BigDecimal.valueOf(89.0),
          BigDecimal.valueOf(99.0), 2.2f, 5.1f, Type.SHORT_SWORD, Rarity.RARE,
          1L, null);
      String query = QueryHelper.buildQueryString(filter);
      mockMvc.perform(get(item_BASE_URL + query)
              .header("Authorization", adminToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content").isNotEmpty())
          .andExpect(jsonPath("$.content.size()").value(1))
          .andExpect(jsonPath("$.content[0].name").value(filter.name()))
          .andExpect(jsonPath("$.content[0].material").value(filter.material().name()))
          .andExpect(jsonPath("$.content[0].type").value(filter.type().name()))
          .andExpect(jsonPath("$.content[0].rarity").value(filter.rarity().name()))
          .andExpect(jsonPath("$.content[*].active", hasItem(true)));
    }

    @Test
    @DisplayName("Can get all active itens with admin acount")
    void getAllActiveItems_canGetAllActiveItensSuccessfully_withAdminAccount() throws Exception {
      mockMvc.perform(get(item_BASE_URL)
              .header("Authorization", adminToken)
              .param("active", "true"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content").isNotEmpty())
          .andExpect(jsonPath("$.content[*].active").value(everyItem(is(true))));
    }

    @Test
    @DisplayName("Can get all unactive itens with admin acount")
    void getAllActiveItems_canGetAllUnactiveItensSuccessfully_withAdminAccount() throws Exception {
      mockMvc.perform(get(item_BASE_URL)
              .header("Authorization", adminToken)
              .param("active", "false"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content").isNotEmpty())
          .andExpect(jsonPath("$.content[*].active").value(everyItem(is(false))));
    }

    @Test
    @DisplayName("Can delete an itens with admin acount")
    void deleteItem_canDeleteAnItem_WithAdminAccount() throws Exception {
      Item savedItem = saveItem(MockItem.newItem());
      mockMvc.perform(delete(item_BASE_URL + "/" + savedItem.getId())
              .header("Authorization", adminToken))
          .andExpect(status().isNoContent());
      assertFalse(itemRepository.existsById(savedItem.getId()));
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class ItemControllerExceptionPath {

    @Test
    @DisplayName("Create should return 400 when finalPrice greater than basePrice")
    void createItem_shouldReturn400_whenFinalPriceIsGreater() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder()
          .basePrice(BigDecimal.valueOf(100))
          .finalPrice(BigDecimal.valueOf(200))
          .build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when name is invalid")
    void createItem_shouldReturn400_whenInvalidName() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().name("a").build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when material is null")
    void createItem_shouldReturn400_whenMaterialIsNull() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().material(null).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when base damage is negative")
    void createItem_shouldReturn400_whenBaseDamageIsNegative() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().baseDamage(-1).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when base defense is negative")
    void createItem_shouldReturn400_whenBaseDefenseIsNegative() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().baseDefense(-1).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when base price is negative")
    void createItem_shouldReturn400_whenBasePriceIsNegative() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder()
          .basePrice(BigDecimal.valueOf(-1)).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when final price is negative")
    void createItem_shouldReturn400_whenFinalPriceIsNegative() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder()
          .finalPrice(BigDecimal.valueOf(-1)).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when description is invalid")
    void createItem_shouldReturn400_whenInvalidDescription() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().description("aaaaaaaaa").build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when weight is negative")
    void createItem_shouldReturn400_whenWeightIsNegative() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder()
          .weight(-1d).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when stock is negative")
    void createItem_shouldReturn400_whenStockIsNegative() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder()
          .stock(-1).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when type is null")
    void createItem_shouldReturn400_whenTypeIsNull() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().type(null).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when rarity is null")
    void createItem_shouldReturn400_whenRarityIsNull() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().rarity(null).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 400 when blacksmith is null")
    void createItem_shouldReturn400_whenBlacksmithIsNull() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().blacksmithId(null).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create should return 404 when blacksmith not exists")
    void createItem_shouldReturn404_whenBlacksmithNotExists() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().blacksmithId(999L).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create itemWithId should return 403 when userWithId is not admin")
    void createItem_shouldReturn403_whenUserIsNotAdmin() throws Exception {
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(MockItem.itemRequestDto())))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get itemWithId Should return 404 when itemWithId not found")
    void getItem_shouldReturn404_whenNotFound() throws Exception {
      mockMvc.perform(get(item_BASE_URL + "/999999")
              .header("Authorization", userToken))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update should return 404 when updating non-existing itemWithId")
    void update_shouldReturn404_whenItemNotFound() throws Exception {
      mockMvc.perform(put(item_BASE_URL + "/999999")
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(MockItem.itemRequestDto())))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update should return 400 when updating with final price greater than base price")
    void update_shouldReturn400_whenFinalPriceGreaterThanBasePrice() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder()
          .basePrice(BigDecimal.valueOf(100))
          .finalPrice(BigDecimal.valueOf(200))
          .build();
      mockMvc.perform(put(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update should return 404 when blacksmith not exists")
    void updateItem_shouldReturn404_whenBlacksmithNotExists() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().blacksmithId(999L).build();
      mockMvc.perform(put(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Patch update should return 404 when patching non-existing itemWithId")
    void patch_shouldReturn404_whenItemNotFound() throws Exception {
      mockMvc.perform(patch(item_BASE_URL + "/999999")
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(MockItem.itemPatchUpdateDto())))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Patch update should return 400 with invalid itemWithId name")
    void patch_shouldReturn400_withInvalidItemName() throws Exception {
      for (String name :  new String[]{"", "  ", "a"}) {
        ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto().toBuilder().name(name).build();
        mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
                .header("Authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
      }
    }

    @Test
    @DisplayName("Patch update should return 400 with invalid base damage")
    void patch_shouldReturn400_withInvalidBaseDamage() throws Exception {
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto().toBuilder().baseDamage(-1).build();
      mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Patch update should return 400 with invalid base defense")
    void patch_shouldReturn400_withInvalidBaseDefense() throws Exception {
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto().toBuilder().baseDefense(-1).build();
      mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Patch update should return 400 with invalid base price")
    void patch_shouldReturn400_withInvalidBasePrice() throws Exception {
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto().toBuilder()
          .basePrice(BigDecimal.valueOf(-1)).build();
      mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Patch update should return 400 with invalid final price")
    void patch_shouldReturn400_withInvalidFinalPrice() throws Exception {
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto()
          .toBuilder().finalPrice(BigDecimal.valueOf(-1)).build();
      mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Patch update should return 400 with invalid description")
    void patch_shouldReturn400_withInvalidDescription() throws Exception {
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto()
          .toBuilder().description("Too short").build();
      mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Patch update should return 400 with invalid weight")
    void patch_shouldReturn400_withInvalidWeight() throws Exception {
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto()
          .toBuilder().weight(-1f).build();
      mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get itemWithId By Blacksmith orderId Should return 404 when blacksmith not exists")
    void getItemByBlacksmithId_shouldReturn404_whenBlacksmithNotExists() throws Exception {
      mockMvc.perform(get(item_BASE_URL + "/blacksmith/999999")
              .header("Authorization", userToken))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Patch update should return 400 with invalid stock")
    void patch_shouldReturn400_withInvalidStock() throws Exception {
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto()
          .toBuilder().stock(-1).build();
      mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete should return 404 when deleting non-existing itemWithId")
    void delete_shouldReturn404_whenItemNotFound() throws Exception {
      mockMvc.perform(delete(item_BASE_URL + "/999999")
              .header("Authorization", adminToken))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete should return 403 when deleting sold itemWithId")
    void delete_shouldReturn403_whenItemWasSold() throws Exception {
      Item soldItem = saveItem(MockItem.newItem());
      soldItem.addSoldQuantity(10);
      itemRepository.save(soldItem);
      mockMvc.perform(delete(item_BASE_URL + "/" + soldItem.getId())
              .header("Authorization", adminToken))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Delete should return 403 when userWithId tries to delete an itemWithId")
    void delete_shouldReturn403_whenUserIsNotAdmin() throws Exception {
      mockMvc.perform(delete(item_BASE_URL + "/" + item.getId())
              .header("Authorization", userToken))
          .andExpect(status().isForbidden());
    }

  }

  private Item saveItem(Item newItem) {
    if (newItem.getFinalPrice().compareTo(newItem.getBasePrice()) > 0) {
      throw new InvalidItemDataException(
          "Final price \"%s\" cannot be greater than base \"%s\" price"
              .formatted(newItem.getFinalPrice(), newItem.getBasePrice()));
    }
    Blacksmith blacksmith = findBlacksmithById(newItem.getBlacksmithIdSnapshot());
    newItem.setCraftedBy(blacksmith);
    newItem.setBlacksmithIdSnapshot(blacksmith.getId());
    newItem.setBlacksmithNameSnapshot(blacksmith.getName());
    return itemRepository.save(newItem);
  }

  private Blacksmith findBlacksmithById(Long id) {
    return blacksmithRepository.findById(id)
        .orElseThrow(() -> new IllegalStateException("Blacksmith not found in test DB"));
  }
}
