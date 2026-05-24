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

    public static RatingResponseDto fromEntity(Rating dto) {
        return new RatingResponseDto(
            dto.getId(),
            dto.getReviewerUsername(),
            dto.getRatingValue(),
            dto.getReview(),
            dto.getCreatedAt(),
            dto.getUpdatedAt()
        );
    }

}
