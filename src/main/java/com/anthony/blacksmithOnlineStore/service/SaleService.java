package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.exceptions.DataModifyException;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaleService {
  private final ItemService itemService;
  private final ItemRepository itemRepository;

  @Transactional
  public void performSale(Long itemId, int qty) {
    itemService.itemExistesVerifier(itemId);
    int modifiedLines = itemRepository.decrementStockAndIncrementSoldQuantity(itemId, qty);
    if (modifiedLines == 0) {
      throw new DataModifyException("Item have no stock for this operation: " + itemId);
    }
  }
}
