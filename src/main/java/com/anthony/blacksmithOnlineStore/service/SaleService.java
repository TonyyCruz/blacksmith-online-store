package com.anthony.blacksmithOnlineStore.service;

import org.springframework.stereotype.Service;

import com.anthony.blacksmithOnlineStore.exceptions.InsufficientStockException;
import com.anthony.blacksmithOnlineStore.exceptions.core.order.InvalidOrderException;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {
  private final ItemService itemService;
  private final ItemRepository itemRepository;

  public void performSale(long itemId, int qty) {
    itemService.itemExistesVerifier(itemId);
    if (!itemRepository.isItemActive(itemId)) {
      throw new InvalidOrderException("Item %d is unactive".formatted(itemId));
    }
    int modifiedLines = itemRepository.decrementStockAndIncrementSoldQuantity(itemId, qty);
    if (modifiedLines == 0) {
      throw new InsufficientStockException("Item have no stock for this operation: " + itemId);
    }
  }

  public void cancelSale(long itemId, int qty) {
    itemService.itemExistesVerifier(itemId);
    int modifiedLines = itemRepository.incrementStockAndDecrementSoldQuantity(itemId, qty);
    if (modifiedLines == 0) {
      throw new InsufficientStockException("Item have no sufficient sold for this operation: " + itemId);
    }
  }
}
