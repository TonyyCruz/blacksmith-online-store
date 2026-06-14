package com.anthony.blacksmithOnlineStore.helper.mocks;

import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.Rating;

public class MockRating {

  public static Rating rating(OrderItem orderItem) {

    return Rating.builder()
        .id(1L)
        .reviewerUserId(orderItem.getUserId())
        .reviewedItemId(orderItem.getItemId())
        .reviewedBlacksmithId(orderItem.getBlacksmithId())
        .orderItem(orderItem)
        .reviewerUsername("testUser")
        .ratingValue(5)
        .review("Excellent craftsmanship")
        .build();
  }

  public static Rating rating() {

    return rating(MockOrderItem.orderItem());
  }

  public static Rating lowRating(OrderItem orderItem) {

    return Rating.builder()
        .id(2L)
        .reviewerUserId(orderItem.getUserId())
        .reviewedItemId(orderItem.getItemId())
        .reviewedBlacksmithId(orderItem.getBlacksmithId())
        .orderItem(orderItem)
        .reviewerUsername("angryCustomer")
        .ratingValue(1)
        .review("Terrible itemWithId")
        .build();
  }
}
