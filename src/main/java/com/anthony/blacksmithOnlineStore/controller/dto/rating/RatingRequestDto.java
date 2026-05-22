package com.anthony.blacksmithOnlineStore.controller.dto.rating;

import com.anthony.blacksmithOnlineStore.entity.Rating;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RatingRequestDto(
    @Min(value = 1, message = "Invalid order item ID.")
    Long orderItemId,
    @Min(value = 1, message = "Rating must be at least 1.")
    @Max(value = 5, message = "Rating cannot be greater than 5.")
    int rating,
    String review) {

    public static Rating toEntity(RatingRequestDto dto) {
        Rating rating = new Rating();
        rating.setReview(dto.review());
        rating.setRatingValue(dto.rating());
        return rating;
    }

}
