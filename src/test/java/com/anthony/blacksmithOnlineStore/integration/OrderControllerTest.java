package com.anthony.blacksmithOnlineStore.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderPaymentDto;
import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.User;
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
import org.springframework.test.web.servlet.MvcResult;

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
      MvcResult result = mockMvc.perform(post(ORDER_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.orderId").isNotEmpty())
          .andExpect(jsonPath("$.status").value(OrderStatus.PENDING.name()))
          .andExpect(jsonPath("$.items").isArray())
          .andExpect(jsonPath("$.items.size()").value(2))
          .andExpect(jsonPath("$.total").value(expectTotalOrderPrice))
          .andReturn();

      String stringResult = result.getResponse().getContentAsString();
      OrderPaymentDto orderResult = objectMapper.readValue(stringResult, OrderPaymentDto.class);
      orderResult.items().forEach(item -> {
        assertThat(item.productId()).isNotNull();
        assertThat(item.quantity()).isGreaterThan(0);
        assertThat(item.UserId()).isEqualTo(orderResult.userId());
        assertThat(item.orderId()).isEqualTo(item.orderId());
      });
      OrderItemResponseDto orderItemResponseDtoOne = orderResult.items().get(0);
      assertThat(orderItemResponseDtoOne.productName()).isEqualTo("Sword of Valor");
      assertThat(orderItemResponseDtoOne.basePrice()).isEqualByComparingTo("100.00");
      assertThat(orderItemResponseDtoOne.priceApplied()).isEqualByComparingTo("90.00");
      assertThat(orderItemResponseDtoOne.quantity()).isEqualTo(1);
      assertThat(orderItemResponseDtoOne.totalPrice()).isEqualByComparingTo("90.00");
      OrderItemResponseDto orderItemResponseDtoTwo = orderResult.items().get(1);
      System.out.println(orderItemResponseDtoOne.basePrice());
      System.out.println(orderItemResponseDtoOne.basePrice().scale());
      assertThat(orderItemResponseDtoTwo.productName()).isEqualTo("Axe of Light");
      assertThat(orderItemResponseDtoTwo.basePrice()).isEqualByComparingTo("150.00");
      assertThat(orderItemResponseDtoTwo.priceApplied()).isEqualByComparingTo("135.00");
      assertThat(orderItemResponseDtoTwo.quantity()).isEqualTo(2);
      assertThat(orderItemResponseDtoTwo.totalPrice()).isEqualByComparingTo("270.00");
    }
  }

  @Test
  @DisplayName("Can get an existing order successfully")
  void getById_canGetOrderSuccessfully() throws Exception {
    mockMvc.perform(get(ORDER_BASE_URL + "/{id}", 1L)
            .header("Authorization", userToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.status").value(OrderStatus.DELIVERED.name()))
        .andExpect(jsonPath("$.items").isArray())
        .andExpect(jsonPath("$.items.size()").value(2))
        .andExpect(jsonPath("$.total").value(260.00));
  }

  @Test
  @DisplayName("Can get all existing order successfully")
  void getAll_canGetAllOrdersSuccessfully() throws Exception {
    mockMvc.perform(get(ORDER_BASE_URL)
            .header("Authorization", userToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].status").value(OrderStatus.DELIVERED.name()))
        .andExpect(jsonPath("$[0].items").isArray())
        .andExpect(jsonPath("$[0].items.size()").value(2))
        .andExpect(jsonPath("$[0].total").value(260.00))
        .andExpect(jsonPath("$[1].id").value(2L))
        .andExpect(jsonPath("$[1].status").value(OrderStatus.PENDING.name()))
        .andExpect(jsonPath("$[1].items").isArray())
        .andExpect(jsonPath("$[1].items.size()").value(1))
        .andExpect(jsonPath("$[1].total").value(135.00));
  }
}
