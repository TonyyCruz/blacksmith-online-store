package com.anthony.blacksmithOnlineStore.controller;

import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingResponseDto;
import com.anthony.blacksmithOnlineStore.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ratings")
public class RatingController {
  private final RatingService ratingService;

  @PostMapping
  public ResponseEntity<RatingResponseDto> rate(@Valid @RequestBody RatingRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.ratePurchase(dto));
  }

  @GetMapping("/item/{id}")
  public ResponseEntity<Page<RatingResponseDto>> getRatingsFromItemId(
      @PathVariable Long id,
      @PageableDefault(page = 0, size = 5, sort = "id", direction = Direction.DESC)
      Pageable pageable) {
    return ResponseEntity.ok(ratingService.getRatingsFromItemId(id, pageable));
  }
}
