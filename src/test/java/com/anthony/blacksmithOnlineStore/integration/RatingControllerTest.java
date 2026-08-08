package com.anthony.blacksmithOnlineStore.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.controller.dto.login.LoginRequest;
import com.anthony.blacksmithOnlineStore.controller.dto.rating.RatingRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.entity.Rating;
import com.anthony.blacksmithOnlineStore.helper.DatabaseTestHelper;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockRating;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.OrderItemRepository;
import com.anthony.blacksmithOnlineStore.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Tag("integration")
@DisplayName("Integration test for Rating controller")
public class RatingControllerTest extends TestBase {
  private final String RATING_BASE_URL = "/ratings";
  @Autowired
  private DatabaseTestHelper testHelper;
  @Autowired
  private OrderItemRepository orderItemRepository;
  @Autowired
  private RatingRepository ratingRepository;
  private OrderItem orderItem;
  private String userToken;

  @BeforeEach void setup() {
    userToken = performLogin(userLogin);
    orderItem = getDeliveredOrderItem();
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
    "The rate sent must be the same found in the rating rate");
      assertEquals(rating.review(), updatedOrderItem.getRating().getReview(),
          "The review sent must be the same found in the rating review");
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
          .andExpect(jsonPath("$.content[0].rating").value(rating.getRatingValue()))
          .andExpect(jsonPath("$.content[0].review").value(rating.getReview()));
    }

    @Test
    @DisplayName("Users can get rate of an existing order item successfully")
    void user_canGetRateOfAnExistingOrderItemSuccessfully() throws Exception {
      Rating rating = MockRating.rating(orderItem);
      ratingRepository.save(rating);
      mockMvc.perform(get(RATING_BASE_URL + "/orderItem/{id}", orderItem.getId())
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.reviewerUsername").value(userLogin.username()))
          .andExpect(jsonPath("$.rating").value(rating.getRatingValue()))
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

  private OrderItem getDeliveredOrderItem() {
    Order order = testHelper.getNewOrder();
    testHelper.deliveryOrder(order.getId());
    return order.getOrderItems().get(0);
  }
  
}
