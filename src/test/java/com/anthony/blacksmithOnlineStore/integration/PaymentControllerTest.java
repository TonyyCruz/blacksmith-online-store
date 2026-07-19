package com.anthony.blacksmithOnlineStore.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockPayment;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Tag("integration")
@DisplayName("Integration test for Payment controller")
public class PaymentControllerTest extends TestBase {
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private ItemRepository itemRepository;
  private Order order;
  private String userToken;

  @BeforeEach
  void setup() {
    userToken = performLogin(userLogin);
    order = getTestOrder(1L);
  }

  @Nested
  @DisplayName("Happy Path")
  class PaymentHappyPath {

    @Test
    @DisplayName("Can approve a valid order successfuly")
    void approve_canConfirmAPaymentSuccessfully() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(
          MockPayment.creditCard().toBuilder().amount(order.getTotal()).build());
      mockMvc.perform(post("/payments/order/{id}", order.getId())
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isOk());
       Order updatedOrder = getOrderById(order.getId());
       assertEquals(OrderStatus.PAYMENT_APPROVED, updatedOrder.getStatus(),
           "The order status must be approved");
    }
  }

  @Nested
  @DisplayName("Exception Path")
  class PaymentExceptionPath {

    @Test
    @DisplayName("Throws 404 trying pay an order with invalid id")
    void approve_throws404TryingPayAnOrderWithInvalidId() throws Exception {
      mockMvc.perform(post("/payments/order/{id}", 9999999)
              .header("Authorization", userToken).contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(MockPayment.creditCard())))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Cannot pay an order that does not belong to you and return status 403")
    void approve_throws403TryingPayAnOrderThatIsNotYours() throws Exception {
        User anotherUser = userRepository.save(MockUser.user());
        order.setUser(anotherUser);
      mockMvc.perform(post("/payments/order/{id}", order.getId())
              .header("Authorization", userToken).contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(MockPayment.creditCard())))
          .andExpect(status().isForbidden());
    }
  }

  private Order getTestOrder(long id) {
    Order newOrder = getOrderById(id).toBuilder().status(OrderStatus.PENDING).build();
    newOrder.setUser(getUserById(USER_ID));
    return orderRepository.save(newOrder);
  }

  private Order getOrderById(long id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new IllegalStateException("Order not found in test DB"));
  }
}
