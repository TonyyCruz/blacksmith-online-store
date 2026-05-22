package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.exceptions.BlacksmithNotFoundException;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidItemDataException;
import com.anthony.blacksmithOnlineStore.exceptions.ItemNotFoundException;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockBlacksmith;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockItem;
import com.anthony.blacksmithOnlineStore.mapstruct.ItemUpdate;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.anthony.blacksmithOnlineStore.service.BlacksmithService;
import com.anthony.blacksmithOnlineStore.service.ItemService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
  @Mock
  private ItemRepository itemRepository;
  @Mock
  private BlacksmithService blacksmithService;
  @Mock
  private ItemUpdate itemUpdate;
  @InjectMocks
  private ItemService itemService;
  private Item targetItem;

  @BeforeEach
  void setup() {
    targetItem = MockItem.item();
  }

  @Nested
  @DisplayName("Happy Path")
  class ItemServiceHappyPath {

    @Test
    @DisplayName("Create should save item and return response when data is valid")
    void createItem_shouldCreateItemSuccessfully_withValidData() {
      ItemRequestDto dto = MockItem.itemRequestDto();
      Blacksmith blacksmith = MockBlacksmith.blacksmith(dto.blacksmithId());

      when(blacksmithService.findEntityById(dto.blacksmithId())).thenReturn(blacksmith);
      when(itemRepository.save(any())).thenAnswer(i -> i.getArgument(0));

      ItemResponseDto response = itemService.create(dto);

      assertEquals(dto.name(), response.name(), "Name must be equal to receive in dto");
      assertEquals(dto.blacksmithId(), response.blacksmithId(), "Blacksmith Id must be equal to receive in dto");
      assertEquals(dto.baseDefense(), response.baseDefense(), "BaseDefense must be equal to receive in dto");
      assertEquals(dto.baseDamage(), response.baseDamage(), "BaseDamage must be equal to receive in dto");
      assertEquals(dto.basePrice(), response.basePrice(), "BasePrice must be equal to receive in dto");
      assertEquals(dto.finalPrice(), response.finalPrice(), "FinalPrice must be equal to receive in dto");
      assertEquals(dto.description(), response.description(), "Description must be equal to receive in dto");
      assertEquals(dto.material(), response.material(), "Material must be equal to receive in dto");
      assertEquals(dto.rarity(), response.rarity(), "Rarity must be equal to receive);");
      assertEquals(dto.stock(), response.stock(), "Stock must be equal to receive in dto");
      assertEquals(dto.type(), response.type(), "Type must be equal to receive in dto");
      verify(itemRepository, times(1)).save(ItemRequestDto.toEntity(dto));
      verify(blacksmithService, times(1)).findEntityById(dto.blacksmithId());
    }


    @Test
    @DisplayName("Update should modify item and return response when data is valid")
    void updateItem_shouldUpdateItemSuccessfully_withValidData() {
      Long id = targetItem.getId();
      ItemRequestDto dto = MockItem.itemRequestDto();
      Blacksmith blacksmith = MockBlacksmith.blacksmith(dto.blacksmithId());

      when(itemRepository.existsById(id)).thenReturn(true);
      when(itemRepository.getReferenceById(id)).thenReturn(targetItem);
      when(blacksmithService.findEntityById(dto.blacksmithId())).thenReturn(blacksmith);

      ItemResponseDto response = itemService.update(id, dto);

      assertEquals(dto.name(), response.name(), "Name must be equal to receive in dto");
      assertEquals(dto.blacksmithId(), response.blacksmithId(), "Blacksmith Id must be equal to receive in dto");
      assertEquals(dto.baseDefense(), response.baseDefense(), "BaseDefense must be equal to receive in dto");
      assertEquals(dto.baseDamage(), response.baseDamage(), "BaseDamage must be equal to receive in dto");
      assertEquals(dto.basePrice(), response.basePrice(), "BasePrice must be equal to receive in dto");
      assertEquals(dto.finalPrice(), response.finalPrice(), "FinalPrice must be equal to receive in dto");
      assertEquals(dto.description(), response.description(), "Description must be equal to receive in dto");
      assertEquals(dto.material(), response.material(), "Material must be equal to receive in dto");
      assertEquals(dto.rarity(), response.rarity(), "Rarity must be equal to receive);");
      assertEquals(dto.stock(), response.stock(), "Stock must be equal to receive in dto");
      assertEquals(dto.type(), response.type(), "Type must be equal to receive in dto");
      verify(itemRepository, times(1)).existsById(id);
      verify(itemRepository, times(1)).getReferenceById(id);
      verify(blacksmithService, times(1)).findEntityById(dto.blacksmithId());
    }

    @Test
    @DisplayName("Patch update should modify item and return response when data is valid")
    void patchUpdate_shouldPatchItemSuccessfully_withValidData() {
      Long id = targetItem.getId();
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto();
      Blacksmith blacksmith = MockBlacksmith.blacksmith(dto.blacksmithId());

      when(itemRepository.findById(id)).thenReturn(Optional.of(targetItem));
      when(blacksmithService.findEntityById(blacksmith.getId())).thenReturn(blacksmith);
      doNothing().when(itemUpdate).updateItemFromDto(dto, targetItem);

      ItemResponseDto response = itemService.update(id, dto);

      assertNotNull(response, "Response mut not be null");
      verify(itemUpdate, times(1)).updateItemFromDto(dto, targetItem);
      verify(itemRepository, times(1)).findById(targetItem.getId());
      verify(blacksmithService, times(1)).findEntityById(dto.blacksmithId());
    }

    @Test
    @DisplayName("FindById should return item response when item exists")
    void findById_shouldFindItemByIdSuccessfully_whenItemExists() {
      when(itemRepository.findById(targetItem.getId()))
          .thenReturn(Optional.of(targetItem));

      ItemResponseDto response = itemService.findById(targetItem.getId());

      assertEquals(targetItem.getId(), response.id(), "Found item must have the correct ID");
      verify(itemRepository, times(1)).findById(targetItem.getId());
    }

    @Test
    @DisplayName("Delete should remove item when it exists and has no sales")
    void deleteItem_shouldDeleteItemSuccessfully_whenItemExists() {
      when(itemRepository.findById(targetItem.getId()))
          .thenReturn(Optional.of(targetItem));

      itemService.deleteItem(targetItem.getId());

      verify(itemRepository, times(1)).deleteById(targetItem.getId());
    }

    @Test
    @DisplayName("AddRating should update rating count and average when item exists")
    void addRating_shouldAddRatingSuccessfully() {
      when(itemRepository.findById(targetItem.getId()))
          .thenReturn(Optional.of(targetItem));

      itemService.addRating(targetItem.getId(), 5);

      assertEquals(1, targetItem.getRatingCount(), "Item must have the correct rating");
      verify(itemRepository, times(1)).save(targetItem);
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class ItemServiceExceptionPath {

    @Test
    @DisplayName("Create should throw InvalidItemDataException when final price is greater than base price")
    void createItem_shouldThrowException_whenFinalPriceIsGreaterThanBasePrice() {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder()
          .basePrice(BigDecimal.valueOf(100))
          .finalPrice(BigDecimal.valueOf(200))
          .build();

      assertThrows(InvalidItemDataException.class, () -> itemService.create(dto),
          "Create Item should throw an exception when final price is greater than base price");
    }

    @Test
    @DisplayName("Create should throw BlacksmithNotFoundException when blacksmith does not exist")
    void createItem_shouldThrowException_whenBlacksmithNotFound() {
      ItemRequestDto dto = MockItem.itemRequestDto();

      when(blacksmithService.findEntityById(dto.blacksmithId()))
          .thenThrow(new BlacksmithNotFoundException(dto.blacksmithId()));

      assertThrows(BlacksmithNotFoundException.class, () -> itemService.create(dto),
          "Create item must throw an exception when blacksmith was not found");
      verify(blacksmithService, times(1)).findEntityById(dto.blacksmithId());
    }

    @Test
    @DisplayName("Update should throw BlacksmithNotFoundException when blacksmith does not exist")
    void updateItem_shouldThrowException_whenBlacksmithNotFound() {
      ItemRequestDto dto = MockItem.itemRequestDto();

      when(blacksmithService.findEntityById(dto.blacksmithId()))
          .thenThrow(new BlacksmithNotFoundException(dto.blacksmithId()));

      assertThrows(BlacksmithNotFoundException.class, () -> itemService.update(1L, dto),
          "Update item must throw an exception when blacksmith was not found");
      verify(blacksmithService, times(1)).findEntityById(dto.blacksmithId());
    }

    @Test
    @DisplayName("Patch update should throw BlacksmithNotFoundException when blacksmith was not exist")
    void pathUpdate_shouldThrowException_whenBlacksmithNotFound() {
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto();

      when(itemRepository.findById(targetItem.getId())).thenReturn(Optional.of(targetItem));
      doThrow(new BlacksmithNotFoundException(dto.blacksmithId()))
          .when(blacksmithService)
          .findEntityById(dto.blacksmithId());

      assertThrows(BlacksmithNotFoundException.class, () -> itemService.update(targetItem.getId(), dto),
          "Patch update must throw an exception when blacksmith was not found");
      verify(itemRepository, times(1)).findById(targetItem.getId());
      verify(blacksmithService, times(1)).findEntityById(dto.blacksmithId());
    }

    @Test
    @DisplayName("Update should throw InvalidItemDataException when final price is greater than base price")
    void updateItem_shouldThrowInvalidItemDataException_whenItemFinalPriceGreaterThanBasePrice() {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder()
          .basePrice(BigDecimal.valueOf(100)).finalPrice(BigDecimal.valueOf(200)).build();
      assertThrows(InvalidItemDataException.class,() -> itemService.update(1L, dto),
          "Update item must throw an exception when final price is greater than base price");
    }

    @Test
    @DisplayName("Patch update should throw InvalidItemDataException when final price is greater than base price")
    void pathUpdate_shouldThrowInvalidItemDataException_whenItemFinalPriceGreaterThanBasePrice() {
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto().toBuilder()
          .basePrice(BigDecimal.valueOf(100)).finalPrice(BigDecimal.valueOf(200))
          .blacksmithId(null).build();

      when(itemRepository.findById(any())).thenReturn(Optional.of(targetItem));
      doAnswer(invocation -> {
        targetItem.setBasePrice(dto.basePrice());
        targetItem.setFinalPrice(dto.finalPrice());
        return null;
      }).when(itemUpdate).updateItemFromDto(dto, targetItem);

      assertThrows(InvalidItemDataException.class, () -> itemService.update(1L, dto),
          "Patch update must throw an exception when final price is greater than base price");
    }

    @Test
    @DisplayName("Update should throw ItemNotFoundException when item does not exist")
    void updateItem_shouldThrowItemNotFoundException_whenItemNotFound() {
      when(itemRepository.existsById(any())).thenReturn(false);
      assertThrows(ItemNotFoundException.class,
          () -> itemService.update(1L, MockItem.itemRequestDto()),
          "Update item must throw an exception when item to update was not found");
      verify(itemRepository, times(1)).existsById(any());
    }

    @Test
    @DisplayName("Patch update should throw ItemNotFoundException when item does not exist")
    void patchUpdate_shouldThrowItemNotFoundException_whenItemNotFound() {
      when(itemRepository.findById(any())).thenReturn(Optional.empty());
      assertThrows(ItemNotFoundException.class,
          () -> itemService.update(1L, MockItem.itemPatchUpdateDto()),
          "Patch update must throw an exception when item to patch update was not found");
      verify(itemRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Delete should throw ForbiddenOperationException when item has sales")
    void deleteItem_shouldThrowForbiddenOperationException_whenDeletingSoldItem() {
      targetItem.addSoldQuantity(5);

      when(itemRepository.findById(targetItem.getId()))
          .thenReturn(Optional.of(targetItem));

      assertThrows(ForbiddenOperationException.class,
          () -> itemService.deleteItem(targetItem.getId()),
          "Delete item must throw an exception when trying to delete an item that has sales");
      verify(itemRepository, times(1)).findById(targetItem.getId());
    }

    @Test
    @DisplayName("Delete should throw ItemNotFoundException when item does not exist")
    void delete_shouldThrowItemNotFoundException_whenItemNotFound() {
      when(itemRepository.findById(any())).thenReturn(Optional.empty());
      assertThrows(ItemNotFoundException.class, () -> itemService.deleteItem(1L),
          "Create item must throw an exception when trying to delete an item that was not found");
      verify(itemRepository, times(1)).findById(any());
    }
  }

}
