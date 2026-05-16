package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.exceptions.DataModifyException;
import com.anthony.blacksmithOnlineStore.exceptions.ItemNotFoundException;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockItem;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.anthony.blacksmithOnlineStore.service.ItemService;
import com.anthony.blacksmithOnlineStore.service.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {
  @Mock
  private ItemRepository itemRepository;
  @Mock
  private ItemService itemService;
  @InjectMocks
  private SaleService saleService;
  private Item targetItem;

  @BeforeEach
  void setup() {
    targetItem = MockItem.item();
  }

  @Nested
  @DisplayName("Happy Path")
  class SaleServiceHappyPath {

    @Test
    @DisplayName("PerformSale should update sold quantity when item exists and has enough stock")
    void performSale_shouldShouldUpdateQuantitySuccessfully() {
      doNothing().when(itemService).itemExistesVerifier(any());
      when(itemRepository.decrementStockAndIncrementSoldQuantity(targetItem.getId(), 2))
          .thenReturn(1);

      saleService.performSale(targetItem.getId(), 2);

      verify(itemService, times(1)).itemExistesVerifier(targetItem.getId());
      verify(itemRepository, times(1))
          .decrementStockAndIncrementSoldQuantity(targetItem.getId(), 2);
    }

  }

  @Nested
  @DisplayName("Exception Path")
  class SaleServiceExceptionPath {

    @Test
    @DisplayName("PerformSale should throw an ItemNotFoundException when item not exists")
    void performSale_shouldShouldThrowAnException_whenItemNotExists() {
      doThrow(new ItemNotFoundException(targetItem.getId()))
          .when(itemService).itemExistesVerifier(any());

      assertThrows(ItemNotFoundException.class,
          () -> saleService.performSale(targetItem.getId(), 2));
      verify(itemService, times(1)).itemExistesVerifier(targetItem.getId());
    }

    @Test
    @DisplayName("PerformSale should throw an DataModifyException when item have no stock")
    void performSale_shouldShouldThrowAnException_whenItemHaveNoStock() {
      doNothing().when(itemService).itemExistesVerifier(any());
      when(itemRepository.decrementStockAndIncrementSoldQuantity(targetItem.getId(), 2))
          .thenReturn(0);

      assertThrows(DataModifyException.class,
          () -> saleService.performSale(targetItem.getId(), 2));
      verify(itemService, times(1)).itemExistesVerifier(targetItem.getId());
    }

  }

}
