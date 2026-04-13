package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemFilterDto;
import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.exceptions.DataModifyException;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidItemDataException;
import com.anthony.blacksmithOnlineStore.exceptions.ItemNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.anthony.blacksmithOnlineStore.repository.specification.ItemSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ItemService {
  private final ItemRepository itemRepository;
  private final BlacksmithService blacksmithService;

  @Transactional
  public Item create(ItemRequestDto dto) {
    if (dto.finalPrice().compareTo(dto.basePrice()) > 0) {
      throw new InvalidItemDataException("Final price cannot be greater than base price");
    }
    Item item = ItemRequestDto.toEntity(dto);
    Blacksmith blacksmith = blacksmithService.findById(dto.blacksmithId());
    item.setCraftedBy(blacksmith);
    item.setBlacksmithIdSnapshot(blacksmith.getId());
    item.setBlacksmithNameSnapshot(blacksmith.getName());
    return itemRepository.save(item);
  }

  @Transactional
  public Item update(Long id, ItemRequestDto dto) {
    if (dto.finalPrice().compareTo(dto.basePrice()) > 0) {
      throw new InvalidItemDataException("Final price cannot be greater than base price");
    }
    Blacksmith blacksmith = blacksmithService.findById(dto.blacksmithId());
    Item item = getReferenceById(id);
    item.setName(dto.name());
    item.setMaterial(dto.material());
    item.setBaseDamage(dto.baseDamage());
    item.setBaseDefense(dto.baseDefense());
    item.setBasePrice(dto.basePrice());
    item.setFinalPrice(dto.finalPrice());
    item.setDescription(dto.description());
    item.setWeight(dto.weight());
    item.setStock(dto.stock());
    item.setType(dto.type());
    item.setRarity(dto.rarity());
    item.setActive(dto.active());
    item.setCraftedBy(blacksmith);
    item.setBlacksmithIdSnapshot(dto.blacksmithId());
    item.setBlacksmithNameSnapshot(blacksmith.getName());
    return itemRepository.save(item);
  }

  public Item findById(Long id) {
    return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
  }

  public Page<Item> findAll(Pageable pageable) {
    return itemRepository.findAll(pageable);
  }

  public void deactivateItem(Long id) {
    Item item = getReferenceById(id);
    item.setActive(false);
    itemRepository.save(item);
  }

  public void activateItem(Long id) {
    Item item = getReferenceById(id);
    item.setActive(true);
    itemRepository.save(item);
  }

  public void deleteItem(Long id) {
    Item item = findById(id);
    if (item.getSold() > 0) {
      throw new ForbiddenOperationException("Cannot delete an item that has been sold.");
    }
    itemRepository.deleteById(id);
  }

  public Item getReferenceById(Long id) {
    itemExistesVerifier(id);
    return itemRepository.getReferenceById(id);
  }

  public Page<Item> findByBlacksmithId(Long blacksmithId, Pageable pageable) {
    return itemRepository.findByCraftedById(blacksmithId, pageable);
  }

  public Page<Item> findFilteredItems(ItemFilterDto filter, Pageable pageable) {
    Specification<Item> specs = ItemSpecifications.withFilters(filter);
    return itemRepository.findAll(specs, pageable);
  }

  @Transactional
  public void incrementStock(Long itemId, int qty) {
    itemExistesVerifier(itemId);
    int modifiedLines =  itemRepository.incrementStock(itemId, qty);
    if (modifiedLines == 0) {
      throw new DataModifyException("Failed to increment stock for item with id: " + itemId);
    }
  }

  @Transactional
  public void decrementStock(Long itemId, int qty) {
    itemExistesVerifier(itemId);
    int modifiedLines = itemRepository.decrementStock(itemId, qty);
    if (modifiedLines == 0) {
      throw new DataModifyException("Failed to decrement stock for item with id: " + itemId);
    }
  }

  @Transactional
  public void performSale(Long itemId, int qty) {
    Item item = findById(itemId);
    item.addSoldQuantity(qty);
    decrementStock(itemId, qty);
  }

  public void itemExistesVerifier(Long id) {
    if (!itemRepository.existsById(id)) throw new ItemNotFoundException(id);
  }

  public void addRating(Long itemId, int rating) {
    Item item = findById(itemId);
    item.addRating(rating);
    itemRepository.save(item);
  }
}
