package com.anthony.blacksmithOnlineStore.integration;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemFilterDto;
import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import com.anthony.blacksmithOnlineStore.exceptions.BlacksmithNotFoundException;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidItemDataException;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockItem;
import com.anthony.blacksmithOnlineStore.integration.helper.QueryHelper;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.BlacksmithRepository;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.MultiValueMap;

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
  void setUp() throws Exception {
    item = itemRepository.findById(1L)
        .orElseThrow(() -> new IllegalStateException("Item not found in test DB"));
    adminToken = performLogin(adminLogin);
    userToken = performLogin(userLogin);
  }

  @Nested
  @Transactional
  @DisplayName("Happy Path")
  class ItemControllerHappyPath {

    @Test
    @Transactional
    @DisplayName("Can create a new Item successfully")
    void createItem_canCreateItemSuccessfully() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(MockItem.itemRequestDto());
      mockMvc.perform(post(item_BASE_URL)
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
    @Transactional
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
    @Transactional
    @DisplayName("Can update all fields witha PATH update successfully")
    void patchUpdate_canUpdateAllFieldsSuccessfully() throws Exception {
      ItemPatchUpdateDto itemUpdate = MockItem.itemPatchUpdateDto();
      Blacksmith blacksmith = findBlacksmithById(itemUpdate.blacksmithId());
      String valueAsString = objectMapper.writeValueAsString(itemUpdate);
      mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
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
    @Transactional
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
      Blacksmith newBlacksmith = findBlacksmithById(itemUpdate.blacksmithId());
      String valueAsString = objectMapper.writeValueAsString(itemUpdate);
      mockMvc.perform(patch(item_BASE_URL + "/" + item.getId())
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
          .andExpect(jsonPath("$.blacksmithId").value(newBlacksmith.getId()))
          .andExpect(jsonPath("$.blacksmithName").value(newBlacksmith.getName()));

    }

    @Test
    @DisplayName("Can get an Item by id successfully")
    void getItemById_canGetItemSuccessfully() throws Exception {
      mockMvc.perform(get(item_BASE_URL + "/" + item.getId())
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
          .andExpect(jsonPath("$.craftedBy.id").value(item.getCraftedBy().getId()));
    }

    @Test
    @DisplayName("Can get all active itens with user acount when filter is empty")
    void getAllActiveItems_canGetAllActiveItensSuccessfully_withUserAccount() throws Exception {
      mockMvc.perform(get(item_BASE_URL)
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content").isNotEmpty())
          .andExpect(jsonPath("$.content.size()").value(8))
          .andExpect(jsonPath("$.content[*].active").value(everyItem(is(true))));
    }

    @Test
    @DisplayName("User can get only active itens")
    void getAllFilteredItems_canGetOnlyActiveItens_withUserAccount() throws Exception {
      mockMvc.perform(get(item_BASE_URL)
              .header("Authorization", userToken)
              .param("active", "false"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content").isNotEmpty())
          .andExpect(jsonPath("$.content[*].active").value(everyItem(is(true))));
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
      mockMvc.perform(get(item_BASE_URL)
              .header("Authorization", adminToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content").isNotEmpty())
          .andExpect(jsonPath("$.content.size()").value(10))
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
      Item savedItem = saveItem(MockItem.itemRequestDto());
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
    @DisplayName("Should return 400 when finalPrice greater than basePrice")
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
    @DisplayName("Should return 400 when name is invalid")
    void createItem_shouldReturn400_whenInvalidName() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().name("a").build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when material is null")
    void createItem_shouldReturn400_whenMaterialIsNull() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().material(null).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when base damage is negative")
    void createItem_shouldReturn400_whenBaseDamageIsNegative() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().baseDamage(-1).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when base defense is negative")
    void createItem_shouldReturn400_whenBaseDefenseIsNegative() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().baseDefense(-1).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when base price is negative")
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
    @DisplayName("Should return 400 when final price is negative")
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
    @DisplayName("Should return 400 when description is invalid")
    void createItem_shouldReturn400_whenInvalidDescription() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().description("aaaaaaaaa").build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when weight is negative")
    void createItem_shouldReturn400_whenWeightIsNegative() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder()
          .weight(-1f).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when stock is negative")
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
    @DisplayName("Should return 400 when type is null")
    void createItem_shouldReturn400_whenTypeIsNull() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().type(null).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when rarity is null")
    void createItem_shouldReturn400_whenRarityIsNull() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().rarity(null).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when blacksmith is null")
    void createItem_shouldReturn400_whenBlacksmithIsNull() throws Exception {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder().blacksmithId(null).build();
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(dto)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create item should return 403 when user is not admin")
    void createItem_shouldReturn403_whenUserIsNotAdmin() throws Exception {
      mockMvc.perform(post(item_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(MockItem.itemRequestDto())))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 404 when item not found")
    void getItem_shouldReturn404_whenNotFound() throws Exception {
      mockMvc.perform(get(item_BASE_URL + "/999999")
              .header("Authorization", userToken))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 when updating non-existing item")
    void update_shouldReturn404_whenItemNotFound() throws Exception {
      mockMvc.perform(put(item_BASE_URL + "/999999")
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(MockItem.itemRequestDto())))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when updating with invalid price")
    void update_shouldReturn400_whenInvalidPrice() throws Exception {
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
    @DisplayName("Should return 404 when patching non-existing item")
    void patch_shouldReturn404_whenItemNotFound() throws Exception {
      mockMvc.perform(patch(item_BASE_URL + "/999999")
              .header("Authorization", adminToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(MockItem.itemPatchUpdateDto())))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existing item")
    void delete_shouldReturn404_whenItemNotFound() throws Exception {
      mockMvc.perform(delete(item_BASE_URL + "/999999")
              .header("Authorization", adminToken))
          .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("Should return 403 when deleting sold item")
    void delete_shouldReturn403_whenItemWasSold() throws Exception {
      Item soldItem = saveItem(MockItem.itemRequestDto());
      soldItem.addSoldQuantity(10);
      itemRepository.save(soldItem);
      mockMvc.perform(delete(item_BASE_URL + "/" + soldItem.getId())
              .header("Authorization", adminToken))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 403 when user tries to delete an item")
    void delete_shouldReturn403_whenUserIsNotAdmin() throws Exception {
      mockMvc.perform(delete(item_BASE_URL + "/" + item.getId())
              .header("Authorization", userToken))
          .andExpect(status().isForbidden());
    }

  }

  private Item saveItem(ItemRequestDto dto) {
    if (dto.finalPrice().compareTo(dto.basePrice()) > 0) {
      throw new InvalidItemDataException("Final price cannot be greater than base price");
    }
    Item item = ItemRequestDto.toEntity(dto);
    Blacksmith blacksmith = findBlacksmithById(dto.blacksmithId());
    item.setCraftedBy(blacksmith);
    item.setBlacksmithIdSnapshot(blacksmith.getId());
    item.setBlacksmithNameSnapshot(blacksmith.getName());
    return itemRepository.save(item);
  }

  private Blacksmith findBlacksmithById(Long id) {
    return blacksmithRepository.findById(id)
        .orElseThrow(() -> new IllegalStateException("Blacksmith not found in test DB"));
  }
}
