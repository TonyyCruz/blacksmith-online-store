package com.anthony.blacksmithOnlineStore.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingResponseDto;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.Rating;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.RatingException;
import com.anthony.blacksmithOnlineStore.repository.RatingRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService {
  private final RatingRepository ratingRepository;
  private final OrderItemService orderItemService;
  private final BlacksmithService blacksmithService;
  private final ItemService itemService;
  private final UserService userService;

  @Transactional
  public RatingResponseDto ratePurchase(RatingRequestDto dto) {
    OrderItem orderItem = orderItemService.findEntityById(dto.orderItemId());
    if (orderItem.getOrder().getDeliveredAt() == null) {
      throw new RatingException("You cannot rate a item that has not been delivered.");
    }
    User user = userService.getUserEntity();
    verifyUserCanRatePurchase(user.getId(), orderItem);
    blacksmithService.addRating(orderItem.getBlacksmithId(), dto.rating());
    itemService.addRating(orderItem.getItemId(), dto.rating());
    Rating rating = RatingRequestDto.toEntity(dto);
    rating.setOrderItem(orderItem);
    rating.setReviewerUserId(user.getId());
    rating.setReviewerUsername(user.getUsername());
    rating.setReviewedItemId(orderItem.getItemId());
    rating.setReviewedBlacksmithId(orderItem.getBlacksmithId());
    rating.setReview(rating.getReview());
    orderItem.setRating(rating);
    return RatingResponseDto.fromEntity(ratingRepository.save(rating));
  }

  public Page<RatingResponseDto> getRatingsFromItemId(Long itemId, Pageable pageable) {
    itemService.itemExistesVerifier(itemId);
    Page<Rating> ratings = ratingRepository.findAllByReviewedItemId(itemId, pageable);
    return ratings.map(RatingResponseDto::fromEntity);
  }

  private void verifyUserCanRatePurchase(UUID userId, OrderItem orderItem) {
    if (!orderItem.getUserId().equals(userId)) {
      throw new ForbiddenOperationException("Only hwo purchased the item can rate it.");
    }
    if (orderItem.getRating() != null) {
      throw new ForbiddenOperationException("This item has already been rated.");
    }
    if (!orderItem.getOrder().getStatus().isFinalState()) {
      throw new ForbiddenOperationException("Cannot rate an item from a not finalized order.");
    }
  }
}
