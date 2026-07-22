package com.anthony.blacksmithOnlineStore.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.anthony.blacksmithOnlineStore.controller.dto.login.LoginRequest;
import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.Rating;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockOrderItem;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockRating;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.anthony.blacksmithOnlineStore.repository.OrderItemRepository;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import com.anthony.blacksmithOnlineStore.repository.RatingRepository;

@Tag("integration")
@DisplayName("Integration test for Rating controller")
public class RatingControllerTest extends TestBase{
  private final String RATING_BASE_URL = "/ratings";
  @Autowired
  private OrderItemRepository orderItemRepository;
  @Autowired
  private ItemRepository itemRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private RatingRepository ratingRepository;
  private OrderItem orderItem;
  private String userToken;
  private User user;

  @BeforeEach void setup() {
    userToken = performLogin(userLogin);
    orderItem = newOrderItem();
    user = getUserById(USER_ID);
  }

  @Nested
  @DisplayName("Happy Path")
  class RatingControllerHappyPath {
      
    @Test
    @DisplayName("Can rate a bought item successfully with correct data")
    void user_canRateAnItemBoughtSuccessfully_withCorrectData() throws Exception {
      RatingRequestDto rating = new RatingRequestDto(orderItem.getId(), 4, "my review");
      String valueAsString = objectMapper.writeValueAsString(rating);
      mockMvc.perform(post(RATING_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").exists())
          .andExpect(jsonPath("$.purchaseUsername").value(user.getUsername()))
          .andExpect(jsonPath("$.rating").value(rating.rating()))
          .andExpect(jsonPath("$.review").value(rating.review()));
    }

    @Test
    @DisplayName("Users get rates of an existing item successfully")
    void user_canGetRatesOfAnExistingItemSuccessfully() throws Exception {
      Rating rating = MockRating.rating(orderItem);
      ratingRepository.save(rating);
      mockMvc.perform(get(RATING_BASE_URL + "/item/{id}", orderItem.getItemId())
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content[0].id").value(rating.getId().toString()));
    }

  }

  @Nested
  @DisplayName("Exception Path")
  class RatingControllerExceptionPath {

    @Test
    @DisplayName("Cannot rate a bought item that is not yours")
    void user_cannotRateABoughtThatIsNotYours() throws Exception {
      performSaveUser(MockUser.user());
      String token = performLogin(new LoginRequest(MockUser.user().getUsername(), MockUser.user().getPassword()));
      RatingRequestDto rating = new RatingRequestDto(orderItem.getId(), 4, "my review");
      String valueAsString = objectMapper.writeValueAsString(rating);
      mockMvc.perform(post(RATING_BASE_URL)
              .header("Authorization", token)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Cannot rate a bought item that is already rated")
    void user_cannotRateABoughtThatIsAlreadyRated() throws Exception {
      Rating rtg = rate(orderItem.getId(), MockRating.rating());
      OrderItem orderItm = getOrderItem(orderItem.getId());
      //orderItem.setRating(rtg);
      //orderItemRepository.save(orderItem);
      RatingRequestDto rating = new RatingRequestDto(orderItem.getId(), 4, "my review");
      String valueAsString = objectMapper.writeValueAsString(rating);
      mockMvc.perform(post(RATING_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden());
    }
  }

  private OrderItem newOrderItem() {
    Item itm = getItem();
    OrderItem oi = MockOrderItem.fromItem(itm, 1);
    oi.setUserId(USER_ID);
    oi.setOrder(getNormalizedOrder());
    return orderItemRepository.save(oi);
  }

  private Order getNormalizedOrder() {
    Order order = getOrder();
    Order updatedOrder = order.toBuilder()
        .status(OrderStatus.DELIVERED)
        .deliveredAt(LocalDateTime.now())
        .user(getUserById(USER_ID))
        .build();
    return orderRepository.save(updatedOrder);
  }

  private Item getItem() {
    return itemRepository.findById(1L).orElseThrow(()-> new IllegalArgumentException(
        "Item not found in test DB"));
  }

  private Order getOrder() {
    return orderRepository.findById(1L).orElseThrow(()-> new IllegalArgumentException(
        "Order not found in test DB"));
  }

  private OrderItem getOrderItem(Long id) {
    return orderItemRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(
        "Order Item not found in test DB"));
  }

  private Rating rate(Long orderItemId, Rating rating) {
    OrderItem oi = getOrderItem(orderItemId);
    rating.setOrderItem(orderItem);
    return ratingRepository.save(rating);
    //oi.setRating(rating);
    //orderItemRepository.save(oi);
  }
}
