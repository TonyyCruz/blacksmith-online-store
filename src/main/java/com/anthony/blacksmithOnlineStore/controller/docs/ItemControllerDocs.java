package com.anthony.blacksmithOnlineStore.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBadRequestDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBadValidationRequestDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBusinessViolationDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiForbiddenDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiNotFoundDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiUnauthorizedDoc;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemFilterDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Items Controller", description = "Item management")
public interface ItemControllerDocs {

  @ApiResponse(
    responseCode = "201",
    description = "Item created successfully",
    content = @Content(
    schema = @Schema(implementation = ItemResponseDto.class)))
  @ApiNotFoundDoc
  @ApiForbiddenDoc
  @ApiBadRequestDoc
  @ApiBusinessViolationDoc
  @ApiBadValidationRequestDoc
  @Operation(summary = "Create item, ADMIN only")
  public ResponseEntity<ItemResponseDto> createItem(ItemRequestDto dto);

  @ApiResponse(
    responseCode = "200",
    description = "Item updated successfully",
    content = @Content(
    schema = @Schema(implementation = ItemResponseDto.class)))
  @ApiNotFoundDoc
  @ApiForbiddenDoc
  @ApiBadRequestDoc
  @ApiBusinessViolationDoc
  @ApiBadValidationRequestDoc
  @Operation(summary = "Update all item data by id, ADMIN only")
  public ResponseEntity<ItemResponseDto> updateItem(Long id, ItemRequestDto dto);

  @ApiResponse(
    responseCode = "200",
    description = "Item updated successfully",
    content = @Content(
    schema = @Schema(implementation = ItemResponseDto.class)))
  @ApiNotFoundDoc
  @ApiForbiddenDoc
  @ApiBadRequestDoc
  @ApiBusinessViolationDoc
  @ApiBadValidationRequestDoc
  @Operation(summary = "Update item partially by id, ADMIN only")
  public ResponseEntity<ItemResponseDto> patchItemUpdate(Long id, ItemPatchUpdateDto dto);

  @ApiResponse(
    responseCode = "200",
    description = "Item found successfully",
    content = @Content(
    schema = @Schema(implementation = ItemResponseDto.class)))
  @ApiNotFoundDoc
  @ApiUnauthorizedDoc
  @Operation(summary = "Find an active item by id")
  public ResponseEntity<ItemResponseDto> getItemById(Long id);

  @ApiResponse(
    responseCode = "200",
    description = "Page of found items")
  @ApiUnauthorizedDoc
  @Operation(summary = "Find all items by filter, only admin can get inactive items")
  public ResponseEntity<Page<ItemResponseDto>> getAllFilteredItems(ItemFilterDto filter, Pageable pageable);

  @ApiResponse(
    responseCode = "204",
    description = "Item deleted successfully")
  @ApiBadRequestDoc
  @ApiForbiddenDoc
  @Operation(summary = "Delete item by id, ADMIN only")
  public ResponseEntity<Void> deleteItem(Long id);
}
