package com.anthony.blacksmithOnlineStore.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockBlacksmith;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockOrder;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Tag("integration")
@DisplayName("Integration test for Order controller")
public class OrderControllerTest extends TestBase {
  private final String ORDER_BASE_URL = "/orders";
  @Autowired
  private OrderRepository orderRepository;
  private String userToken;
//  private Order order;

  @BeforeEach
  void setup() {
//    order = orderRepository.findById(1L)
//        .orElseThrow(() -> new IllegalStateException("Order not found in test DB"));
    userToken = performLogin(userLogin);
  }

  @Nested
  @Transactional
  @DisplayName("Happy Path")
  class OrderControllerHappyPath {

    @Test
    @Transactional
    @DisplayName("Can create an order successfully")
    void createOrder_canCreateOrderSuccessfully() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(MockOrder.orderRequestDto());
      double expectTotalOrderPrice = 360.00;
      mockMvc.perform(post(ORDER_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.orderId").isNotEmpty())
          .andExpect(jsonPath("$.status").value(OrderStatus.PENDING.name()))
          .andExpect(jsonPath("$.total").value(expectTotalOrderPrice));
    }
  }
}
