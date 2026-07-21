package com.anthony.blacksmithOnlineStore.controller.dto.rating;

import com.anthony.blacksmithOnlineStore.entity.Rating;
import java.math.BigInteger;
import java.time.LocalDateTime;

public record RatingResponseDto(
    Long id,
    String purchaseUsername,
    int rating,
    String review,
    LocalDateTime createdAt,
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
