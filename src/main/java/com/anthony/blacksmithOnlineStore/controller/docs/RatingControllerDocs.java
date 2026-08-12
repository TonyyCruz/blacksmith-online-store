package com.anthony.blacksmithOnlineStore.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBusinessViolationDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.SecuredApiResponses;
import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Rating", description = "Rating management")
public interface RatingControllerDocs {

  @ApiResponse(
    responseCode = "201",
    description = "Rating created successfully"
  )
  @ApiBusinessViolationDoc
  @SecuredApiResponses
  @Operation(summary = "Rate your recived item")
  public ResponseEntity<Void> rate(RatingRequestDto dto);

  @ApiResponse(
    responseCode = "200",
    description = "Rating found successfully",
    content = @Content(
    schema = @Schema(implementation = RatingResponseDto.class)
  ))
  @SecuredApiResponses
  @Operation(summary = "Find the rate by order item id")
  public ResponseEntity<RatingResponseDto> getRatingFromOrderItemId(Long id);

  @ApiResponse(
    responseCode = "200",
    description = "Ratings from item"
  )
  @SecuredApiResponses
  @Operation(summary = "Find all items rate by item id")
  public ResponseEntity<Page<RatingResponseDto>> getRatingsFromItemId(Long id, Pageable pageable);
}
