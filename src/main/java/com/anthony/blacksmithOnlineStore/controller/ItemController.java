package com.anthony.blacksmithOnlineStore.controller;

import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemFilterDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemResponseDto;
import com.anthony.blacksmithOnlineStore.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Items", description = "Item management")
public class ItemController {
  private final ItemService itemService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create item, ADMIN only")
  public ResponseEntity<ItemResponseDto> createItem(@RequestBody @Valid ItemRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(dto));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update all item data by id, ADMIN only")
  public ResponseEntity<ItemResponseDto> updateItem(
      @PathVariable Long id,
      @RequestBody @Valid ItemRequestDto dto) {
    return ResponseEntity.ok(itemService.update(id, dto));
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update item partially by id, ADMIN only")
  public ResponseEntity<ItemResponseDto> patchItemUpdate(
      @PathVariable Long id,
      @RequestBody @Valid ItemPatchUpdateDto dto) {
    return ResponseEntity.ok(itemService.update(id, dto));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find an active item by id")
  public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long id) {
    return ResponseEntity.ok(itemService.findById(id));
  }

  @GetMapping
  @Operation(summary = "Find all items by filter, only admin can get inactive items")
  public ResponseEntity<Page<ItemResponseDto>> getAllFilteredItems(
      @ParameterObject ItemFilterDto filter,
      @PageableDefault(page = 0, size = 20, sort = "id", direction = Direction.DESC)
      @ParameterObject Pageable pageable) {
    return ResponseEntity.ok(itemService.findFilteredItems(filter, pageable));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete item by id, ADMIN only")
  public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
    itemService.deleteItem(id);
    return ResponseEntity.noContent().build();
  }
}
