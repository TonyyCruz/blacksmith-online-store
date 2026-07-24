package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemFilterDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidItemDataException;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentNotFoundException;
import com.anthony.blacksmithOnlineStore.mapstruct.ItemUpdate;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.anthony.blacksmithOnlineStore.repository.specification.ItemSpecifications;
import com.anthony.blacksmithOnlineStore.security.utils.AuthenticatedUserService;
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
  private final ItemUpdate itemUpdate;
  private final AuthenticatedUserService authUser;

  @Transactional
  public ItemResponseDto create(ItemRequestDto dto) {
    Item item = ItemRequestDto.toEntity(dto);
    Blacksmith blacksmith = blacksmithService.findEntityById(dto.blacksmithId());
    item.setCraftedBy(blacksmith);
    item.setBlacksmithIdSnapshot(blacksmith.getId());
    item.setBlacksmithNameSnapshot(blacksmith.getName());
    return ItemResponseDto.fromEntity(itemRepository.save(item));
  }

  @Transactional
  public ItemResponseDto update(Long id, ItemRequestDto dto) {
    Blacksmith blacksmith = blacksmithService.findEntityById(dto.blacksmithId());
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
    return ItemResponseDto.fromEntity(item);
  }

  @Transactional
  public ItemResponseDto update(Long id, ItemPatchUpdateDto dto) {
    Item item = findEntityById(id);
    if (dto.blacksmithId() != null) {
      Blacksmith blacksmith = blacksmithService.findEntityById(dto.blacksmithId());
      item.setCraftedBy(blacksmith);
      item.setBlacksmithIdSnapshot(blacksmith.getId());
      item.setBlacksmithNameSnapshot(blacksmith.getName());
    }
    itemUpdate.updateItemFromDto(dto, item);
    if (item.getFinalPrice().compareTo(item.getBasePrice()) > 0) {
      throw new InvalidItemDataException("Final price cannot be greater than base price");
    }
    return ItemResponseDto.fromEntity(item);
  }

  public ItemResponseDto findById(Long id) {
    return ItemResponseDto.fromEntity(findEntityById(id));
  }

  public Item findEntityById(Long id) {
    return itemRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
  }

  public Page<ItemResponseDto> findFilteredItems(ItemFilterDto filter, Pageable pageable) {
    if (!authUser.isAdmin()) filter = filter.withActiveTrue();
    Specification<Item> specification = ItemSpecifications.withFilters(filter);
    Page<Item> items = itemRepository.findAll(specification, pageable);
    return items.map(ItemResponseDto::fromEntity);
  }

  @Transactional
  public void deleteItem(Long id) {
    Item item = findEntityById(id);
    if (item.getSold() > 0) {
      throw new ForbiddenOperationException("Cannot delete an item that has been sold.");
    }
    itemRepository.deleteById(id);
  }

  public Item getReferenceById(Long id) {
    itemExistesVerifier(id);
    return itemRepository.getReferenceById(id);
  }

  public void itemExistesVerifier(Long id) {
    if (!itemRepository.existsById(id)) throw new PaymentNotFoundException(id);
  }

  public void addRating(Long itemId, int rating) {
    Item item = findEntityById(itemId);
    item.addRating(rating);
    itemRepository.save(item);
  }
}
