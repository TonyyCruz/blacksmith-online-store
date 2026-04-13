package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.controler.dto.rating.RatingRequestDto;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.Rating;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.repository.RatingRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {
  private final RatingRepository ratingRepository;
  private final OrderItemService orderItemService;
  private final BlacksmithService blacksmithService;
  private final ItemService itemService;
  private final UserService userService;

  @Transactional
  public void ratePurchase(RatingRequestDto dto, Authentication auth) {
    OrderItem orderItem = orderItemService.findById(dto.orderItemId());
    User user = userService.getUserFromAuth(auth);
    verifyUserCanRatePurchase(user.getId(), orderItem);
    blacksmithService.addRating(orderItem.getBlacksmithId(), dto.rating());
    itemService.addRating(orderItem.getItemId(), dto.rating());
    Rating rating = RatingRequestDto.toEntity(dto);
    rating.setOrderItem(orderItem);
    rating.setReviewerUserId(user.getId());
    rating.setReviewerUsername(user.getUsername());
    rating.setReviewedItemId(orderItem.getItemId());
    rating.setReviewedBlacksmithId(orderItem.getBlacksmithId());
    ratingRepository.save(rating);
  }

  public Page<Rating> getRatingsForItemId(Long itemId, Pageable pageable) {
    itemService.itemExistesVerifier(itemId);
    return ratingRepository.findAllByReviewedItemId(itemId, pageable);
  }

  private void verifyUserCanRatePurchase(UUID userId, OrderItem orderItem) {
    if (!orderItem.getUserId().equals(userId)) {
      throw new ForbiddenOperationException("Only hwo purchased the item can rate it.");
    }
    if (orderItem.isReviewed()) {
      throw new ForbiddenOperationException("This item has already been rated.");
    }
    if (!orderItem.getOrder().getStatus().isFinalState()) {
      throw new ForbiddenOperationException("Cannot rate an item from a not finalized order.");
    }
  }
}
