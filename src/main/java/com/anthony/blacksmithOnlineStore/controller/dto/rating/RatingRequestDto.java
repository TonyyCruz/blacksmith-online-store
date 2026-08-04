package com.anthony.blacksmithOnlineStore.controller.dto.rating;

import com.anthony.blacksmithOnlineStore.entity.Rating;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RatingRequestDto(
  @Schema(description = "The order item id", example = "1")
  @Min(value = 1, message = "Invalid order item ID.")
  Long orderItemId,
  @Schema(description = "The rating value", example = "4.5")
  @Min(value = 1, message = "Rating must be at least 1")
  @Max(value = 5, message = "Rating cannot be greater than 5")
  int rating,
  @Schema(description = "The review text", example = "Such a great item")
  String review) {

  public static Rating toEntity(RatingRequestDto dto) {
    Rating rating = new Rating();
    rating.setReview(dto.review());
    rating.setRatingValue(dto.rating());
    return rating;
  }

}
