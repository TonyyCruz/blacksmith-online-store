package com.anthony.blacksmithOnlineStore.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anthony.blacksmithOnlineStore.controller.dto.login.LoginRequest;
import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.Rating;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockOrderItem;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockRating;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.BlacksmithRepository;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.anthony.blacksmithOnlineStore.repository.OrderItemRepository;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import com.anthony.blacksmithOnlineStore.repository.RatingRepository;

import jakarta.persistence.EntityManager;

@Tag("integration")
@DisplayName("Integration test for Rating controller")
public class RatingControllerTest extends TestBase {
  private final String RATING_BASE_URL = "/ratings";
  @Autowired
  private OrderItemRepository orderItemRepository;
  @Autowired
  private ItemRepository itemRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private RatingRepository ratingRepository;
  @Autowired
  private BlacksmithRepository blacksmithRepository;
  @Autowired
  private EntityManager entityManager;
  private OrderItem orderItem;
  private String userToken;

  @BeforeEach void setup() {
    userToken = performLogin(userLogin);
    orderItem = getTestOrderItem(1L);
  }

  @Nested
  @DisplayName("Happy Path")
  class RatingControllerHappyPath {
      
    @Test
    @Transactional(propagation = Propagation.NEVER)
    @DisplayName("Can rate an order item delivered successfully with correct data")
    void user_canRateAnOrderItemDeliveredSuccessfully_withCorrectData() throws Exception {
      RatingRequestDto rating = new RatingRequestDto(orderItem.getId(), 4, "my review");
      String valueAsString = objectMapper.writeValueAsString(rating);
      mockMvc.perform(post(RATING_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isCreated());
      OrderItem updatedOrderItem = orderItemRepository.findById(orderItem.getId()).get();
      assertEquals(rating.rating(), updatedOrderItem.getRating().getRatingValue(), 
    "The rate sended must be the same finded in the rating rate");
      assertTrue(rating.review().equals(updatedOrderItem.getRating().getReview()), 
      "The review sended must be the same finded in the rating review");
    }

    @Test
    @DisplayName("Users can get all rates of an existing item successfully")
    void user_canGetAllRatesOfAnExistingItemSuccessfully() throws Exception {
      Rating rating = MockRating.rating(orderItem);
      ratingRepository.save(rating);
      mockMvc.perform(get(RATING_BASE_URL + "/item/{id}", orderItem.getItemId())
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.content").isArray())
          .andExpect(jsonPath("$.content").isNotEmpty())
          .andExpect(jsonPath("$.content[0].reviewerUsername").value(userLogin.username()))
          .andExpect(jsonPath("$.content[0].ratingValue").value(rating.getRatingValue()))
          .andExpect(jsonPath("$.content[0].review").value(rating.getReview()));
    }

    @Test
    @DisplayName("Users get rates of an existing order item successfully")
    void user_canGetRatesOfAnExistingOrderItemSuccessfully() throws Exception {
      Rating rating = MockRating.rating(orderItem);
      ratingRepository.save(rating);
      mockMvc.perform(get(RATING_BASE_URL + "/orderItem/{id}", orderItem.getItemId())
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.reviewerUsername").value(userLogin.username()))
          .andExpect(jsonPath("$.ratingValue").value(rating.getRatingValue()))
          .andExpect(jsonPath("$.review").value(rating.getReview()));
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
      ratingRepository.save(MockRating.rating(orderItem));
      RatingRequestDto rating = new RatingRequestDto(orderItem.getId(), 4, "my review");
      String valueAsString = objectMapper.writeValueAsString(rating);
      mockMvc.perform(post(RATING_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden());
    }
  }

  private OrderItem getTestOrderItem(Long id) {
    Item itm = getItem(1L);
    OrderItem oi = MockOrderItem.fromItem(itm, 1);
    oi.setUserId(USER_ID);
    oi.setOrder(getNormalizedOrder(id));
    return orderItemRepository.save(oi);
  }

  private Order getNormalizedOrder(Long id) {
    Order order = getOrder(id);
    Order updatedOrder = order.toBuilder()
        .status(OrderStatus.DELIVERED)
        .deliveredAt(LocalDateTime.now())
        .user(getUserById(USER_ID))
        .build();
    return orderRepository.save(updatedOrder);
  }

  private Item getItem(Long id) {
    return itemRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(
        "Item not found in test DB"));
  }

  private Order getOrder(Long id) {
    return orderRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(
        "Order not found in test DB"));
  }

  private Blacksmith getBlacksmith(Long id) {
    return blacksmithRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Blacksmith not found in test DB"));
  }

//  private OrderItem getOrderItem(Long id) {
//    return orderItemRepository.findById(id).orElseThrow(()-> new IllegalArgumentException(
//        "Order Item not found in test DB"));
//  }
//
//  private Rating rate(Long orderItemId, Rating rating) {
//    rating.setOrderItem(orderItem);
//    orderItem.setRating(rating);
//    return ratingRepository.save(rating);
//  }
}
