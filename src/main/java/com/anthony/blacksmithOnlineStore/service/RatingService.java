package com.anthony.blacksmithOnlineStore.service;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingResponseDto;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.Rating;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.events.RatingCreatedEvent;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.BusinessViolationException;
import com.anthony.blacksmithOnlineStore.exceptions.ResourceNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.RatingRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService {
  private final RatingRepository ratingRepository;
  private final OrderItemService orderItemService;
  private final ItemService itemService;
  private final UserService userService;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void ratePurchase(RatingRequestDto dto) {
    OrderItem orderItem = orderItemService.findEntityById(dto.orderItemId());
    User user = userService.findUserEntity();
    verifyUserCanRatePurchase(user.getId(), orderItem);
    Rating rating = RatingRequestDto.toEntity(dto);
    rating.setOrderItem(orderItem);
    rating.setReviewerUserId(user.getId());
    rating.setReviewerUsername(user.getUsername());
    rating.setReviewedItemId(orderItem.getItemId());
    rating.setReviewedBlacksmithId(orderItem.getBlacksmithId());
    rating = ratingRepository.save(rating);
    eventPublisher.publishEvent(new RatingCreatedEvent(orderItem.getItemId(), dto.rating(), rating.getId()));
  }

  public Page<RatingResponseDto> getRatingsFromItemId(Long itemId, Pageable pageable) {
    itemService.itemExistesVerifier(itemId);
    Page<Rating> ratings = ratingRepository.findAllByReviewedItemId(itemId, pageable);
    return ratings.map(RatingResponseDto::fromEntity);
  }

  public RatingResponseDto findByOrderItemId(Long id) {
    Rating rating = ratingRepository.findByOrderItemId(id).orElseThrow(
        () -> new ResourceNotFoundException("Order not found with id: %d".formatted(id)));
    return RatingResponseDto.fromEntity(rating);
  }

  private void verifyUserCanRatePurchase(UUID userId, OrderItem orderItem) {
    if (!orderItem.getOrder().wasDelivered()) {
      throw new BusinessViolationException("You cannot rate a item that Was not delivered.");
    }
    if (!orderItem.getUserId().equals(userId)) {
      throw new ForbiddenOperationException("Only hwo purchased the item can rate it.");
    }
    if (ratingRepository.existsByOrderItemId(orderItem.getId())) {
      throw new ForbiddenOperationException("This item has already been rated.");
    }
  }
}
