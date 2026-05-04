package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.exceptions.DataModifyException;
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
    void shouldCreateItemSuccessfully() {
      ItemRequestDto dto = MockItem.itemRequestDto();
      Blacksmith blacksmith = MockBlacksmith.blacksmith(dto.blacksmithId());

      when(blacksmithService.findEntityById(dto.blacksmithId())).thenReturn(blacksmith);
      when(itemRepository.save(any())).thenAnswer(i -> i.getArgument(0));

      ItemResponseDto response = itemService.create(dto);

      assertEquals(dto.name(), response.name(), "Name must be equal to receive in dto");
      assertEquals(dto.blacksmithId(), response.craftedBy().id(), "CraftedBy must be equal to receive in dto");
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
    void shouldUpdateItemSuccessfully() {
      Long id = targetItem.getId();
      ItemRequestDto dto = MockItem.itemRequestDto();
      Blacksmith blacksmith = MockBlacksmith.blacksmith(dto.blacksmithId());

      when(itemRepository.existsById(id)).thenReturn(true);
      when(itemRepository.getReferenceById(id)).thenReturn(targetItem);
      when(blacksmithService.findEntityById(dto.blacksmithId())).thenReturn(blacksmith);
      when(itemRepository.save(any())).thenAnswer(i -> i.getArgument(0));

      ItemResponseDto response = itemService.update(id, dto);

      assertEquals(dto.name(), response.name(), "Name must be equal to receive in dto");
      assertEquals(dto.blacksmithId(), response.craftedBy().id(), "CraftedBy must be equal to receive in dto");
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
      verify(itemRepository, times(1)).save(any());
      verify(blacksmithService, times(1)).findEntityById(dto.blacksmithId());
    }

    @Test
    @DisplayName("Patch update should modify item and return response when data is valid")
    void shouldPatchItemSuccessfully() {
      Long id = targetItem.getId();
      ItemPatchUpdateDto dto = MockItem.itemPatchUpdateDto();
      Blacksmith blacksmith = MockBlacksmith.blacksmith(dto.blacksmithId());

      when(itemRepository.existsById(id)).thenReturn(true);
      when(itemRepository.getReferenceById(id)).thenReturn(targetItem);
      doNothing().when(itemUpdate).updateItemFromDto(dto, targetItem);
      doNothing().when(blacksmithService).existsVerify(dto.blacksmithId());
      when(itemRepository.save(any())).thenAnswer(i -> i.getArgument(0));

      ItemResponseDto response = itemService.update(id, dto);

      assertNotNull(response, "Response mut not be null");
      verify(itemUpdate, times(1)).updateItemFromDto(dto, targetItem);
      verify(itemRepository, times(1)).existsById(id);
      verify(itemRepository, times(1)).getReferenceById(id);
      verify(itemRepository, times(1)).save(any());
      verify(blacksmithService, times(1)).existsVerify(dto.blacksmithId());
    }

    @Test
    @DisplayName("FindById should return item response when item exists")
    void shouldFindItemByIdSuccessfully() {
      when(itemRepository.findById(targetItem.getId()))
          .thenReturn(Optional.of(targetItem));

      ItemResponseDto response = itemService.findById(targetItem.getId());

      assertEquals(targetItem.getId(), response.id());
      verify(itemRepository, times(1)).findById(targetItem.getId());
    }

    @Test
    @DisplayName("Delete should remove item when it exists and has no sales")
    void shouldDeleteItemSuccessfully() {
      when(itemRepository.findById(targetItem.getId()))
          .thenReturn(Optional.of(targetItem));

      itemService.deleteItem(targetItem.getId());

      verify(itemRepository, times(1)).deleteById(targetItem.getId());
    }

    @Test
    @DisplayName("IncrementStock should increase stock when item exists")
    void shouldIncrementStockSuccessfully() {
      when(itemRepository.existsById(targetItem.getId())).thenReturn(true);
      when(itemRepository.incrementStock(targetItem.getId(), 10)).thenReturn(1);

      itemService.incrementStock(targetItem.getId(), 10);

      verify(itemRepository, times(1)).incrementStock(targetItem.getId(), 10);
    }

    @Test
    @DisplayName("DecrementStock should decrease stock when item exists and has enough stock")
    void shouldDecrementStockSuccessfully() {
      when(itemRepository.existsById(targetItem.getId())).thenReturn(true);
      when(itemRepository.decrementStock(targetItem.getId(), 5)).thenReturn(1);

      itemService.decrementStock(targetItem.getId(), 5);

      verify(itemRepository, times(1)).decrementStock(targetItem.getId(), 5);
    }

    @Test
    @DisplayName("PerformSale should update sold quantity when item exists and has enough stock")
    void shouldPerformSaleSuccessfully() {
      when(itemRepository.findById(targetItem.getId()))
          .thenReturn(Optional.of(targetItem));
      when(itemRepository.existsById(targetItem.getId())).thenReturn(true);
      when(itemRepository.decrementStock(targetItem.getId(), 2)).thenReturn(1);

      itemService.performSale(targetItem.getId(), 2);

      assertEquals(2, targetItem.getSold());
      verify(itemRepository, times(1)).findById(targetItem.getId());
      verify(itemRepository, times(1)).existsById(targetItem.getId());
      verify(itemRepository, times(1)).decrementStock(targetItem.getId(), 2);
    }

    @Test
    @DisplayName("AddRating should update rating count and average when item exists")
    void shouldAddRatingSuccessfully() {
      when(itemRepository.findById(targetItem.getId()))
          .thenReturn(Optional.of(targetItem));

      itemService.addRating(targetItem.getId(), 5);

      assertEquals(1, targetItem.getRatingCount());
      verify(itemRepository, times(1)).save(targetItem);
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class ItemServiceExceptionPath {

    @Test
    @DisplayName("Create should throw InvalidItemDataException when final price is greater than base price")
    void shouldThrowExceptionWhenFinalPriceIsGreater() {
      ItemRequestDto dto = MockItem.itemRequestDto().toBuilder()
          .basePrice(BigDecimal.valueOf(100))
          .finalPrice(BigDecimal.valueOf(200))
          .build();

      assertThrows(InvalidItemDataException.class, () -> itemService.create(dto));
    }

    @Test
    @DisplayName("Update should throw InvalidItemDataException when final price is greater than base price")
    void shouldThrowWhenItemNotFoundOnFind() {
      when(itemRepository.findById(any())).thenReturn(Optional.empty());

      assertThrows(ItemNotFoundException.class,
          () -> itemService.findById(1L));
    }

    @Test
    @DisplayName("Update should throw ItemNotFoundException when item does not exist")
    void shouldThrowWhenUpdatingNonExistingItem() {
      when(itemRepository.existsById(any())).thenReturn(false);

      assertThrows(ItemNotFoundException.class,
          () -> itemService.update(1L, MockItem.itemRequestDto()));
    }

    @Test
    @DisplayName("Delete should throw ForbiddenOperationException when item has sales")
    void shouldThrowWhenDeletingSoldItem() {
      targetItem.addSoldQuantity(5);

      when(itemRepository.findById(targetItem.getId()))
          .thenReturn(Optional.of(targetItem));

      assertThrows(ForbiddenOperationException.class,
          () -> itemService.deleteItem(targetItem.getId()));
    }

    @Test
    @DisplayName("IncrementStock should throw DataModifyException when stock update fails")
    void shouldThrowWhenIncrementStockFails() {
      when(itemRepository.existsById(targetItem.getId())).thenReturn(true);
      when(itemRepository.incrementStock(targetItem.getId(), 10)).thenReturn(0);

      assertThrows(DataModifyException.class,
          () -> itemService.incrementStock(targetItem.getId(), 10));
    }

    @Test
    @DisplayName("DecrementStock should throw DataModifyException when stock update fails")
    void shouldThrowWhenDecrementStockFails() {
      when(itemRepository.existsById(targetItem.getId())).thenReturn(true);
      when(itemRepository.decrementStock(targetItem.getId(), 5)).thenReturn(0);

      assertThrows(DataModifyException.class,
          () -> itemService.decrementStock(targetItem.getId(), 5));
    }
  }

}
