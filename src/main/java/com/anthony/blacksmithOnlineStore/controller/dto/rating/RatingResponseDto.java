package com.anthony.blacksmithOnlineStore.controller.dto.rating;

import com.anthony.blacksmithOnlineStore.entity.Rating;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record RatingResponseDto(
  @Schema(example = "1")
  Long id,
  @Schema(example = "user")
  String reviewerUsername,
  @Schema(example = "4")
  int rating,
  @Schema(example = "I liked the product, it has a great weight and good balance")
  String review,
  @Schema(example = "2026-05-01T10:05:00")
  LocalDateTime createdAt,
  @Schema(example = "2026-05-01T10:05:00")
  LocalDateTime updatedAt) {

  public static RatingResponseDto fromEntity(Rating rating) {
    return new RatingResponseDto(
      rating.getId(),
      rating.getReviewerUsername(),
      rating.getRatingValue(),
      rating.getReview(),
      rating.getCreatedAt(),
      rating.getUpdatedAt()
    );
  }

}
