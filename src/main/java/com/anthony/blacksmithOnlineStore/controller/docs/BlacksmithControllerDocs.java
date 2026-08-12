package com.anthony.blacksmithOnlineStore.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.anthony.blacksmithOnlineStore.controller.docs.anotations.SecuredApiResponses;
import com.anthony.blacksmithOnlineStore.controller.dto.blacksmith.BlacksmithRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.blacksmith.BlacksmithResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Blacksmith", description = "Blacksmith management")
public interface BlacksmithControllerDocs {

  @ApiResponse(
    responseCode = "201",
    description = "Blacksmith created successfully",
    content = @Content(
    schema = @Schema(implementation = BlacksmithResponseDto.class)
  ))
  @SecuredApiResponses
  @Operation(summary = "Create a blacksmith, ADMIN only")
  public ResponseEntity<BlacksmithResponseDto> createBlacksmith(BlacksmithRequestDto dto);

  @ApiResponse(
    responseCode = "200",
    description = "Blacksmith updated successfully",
    content = @Content(
    schema = @Schema(implementation = BlacksmithResponseDto.class)
  ))
  @SecuredApiResponses
  @Operation(summary = "Update all blacksmith data, ADMIN only")
  public ResponseEntity<BlacksmithResponseDto> updateBlacksmith(BlacksmithRequestDto dto, Long id);

  @ApiResponse(
    responseCode = "200",
    description = "Page of all Blacksmiths"
  )
  @SecuredApiResponses
  @Operation(summary = "Find all blacksmiths")
  public ResponseEntity<Page<BlacksmithResponseDto>> findAll(Pageable pageable);

  @ApiResponse(
    responseCode = "200",
    description = "Blacksmith found successfully",
    content = @Content(
    schema = @Schema(implementation = BlacksmithResponseDto.class)
  ))
  @SecuredApiResponses
  @Operation(summary = "Find blacksmith by id")
  public ResponseEntity<BlacksmithResponseDto> findById(Long id);

  @ApiResponse(
    responseCode = "200",
    description = "Blacksmith search result"
  )
  @SecuredApiResponses
  @Operation(summary = "Find blacksmith by name")
  public ResponseEntity<Page<BlacksmithResponseDto>> findByName(Pageable pageable, String name);
}
