package com.anthony.blacksmithOnlineStore.controler;

import com.anthony.blacksmithOnlineStore.controler.dto.rating.RatingRequestDto;
import com.anthony.blacksmithOnlineStore.controler.dto.rating.RatingResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Rating;
import com.anthony.blacksmithOnlineStore.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
  public ResponseEntity<Void> createRating(
      @Valid @RequestBody RatingRequestDto dto, Authentication auth) {
    ratingService.ratePurchase(dto, auth);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/item/{id}")
  public ResponseEntity<Page<RatingResponseDto>> getRatingsForItem(
      @PathVariable Long id,
      @PageableDefault(page = 0, size = 5, sort = "id", direction = Direction.DESC)
      Pageable pageable) {
    Page<Rating> ratings = ratingService.getRatingsForItemId(id, pageable);
    return ResponseEntity.ok(ratings.map(RatingResponseDto::fromEntity));
  }
}
