package com.anthony.blacksmithOnlineStore.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderPaymentDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemResponseDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockOrder;
import com.anthony.blacksmithOnlineStore.integration.helper.TestBase;
import com.anthony.blacksmithOnlineStore.repository.ItemRepository;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
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
  @Autowired
  private ItemRepository itemRepository;
  private String userToken;

  @BeforeEach
  void setup() {
    userToken = performLogin(userLogin);
  }

  @Nested
  @DisplayName("Happy Path")
  class OrderControllerHappyPath {

    @Test
    @DisplayName("Can create an order successfully")
    void create_canCreateOrderSuccessfully() throws Exception {
      Item item1 = itemRepository.getReferenceById(1L);
      item1.setFinalPrice(BigDecimal.valueOf(10.00));
      item1.setStock(100);
      item1.setActive(true);
      item1 = itemRepository.save(item1);
      Item item2 = itemRepository.getReferenceById(2L);
      item2.setFinalPrice(BigDecimal.valueOf(15.00));
      item2.setStock(100);
      item2.setActive(true);
      item2 = itemRepository.save(item2);
      OrderRequestDto dto = new OrderRequestDto(
          List.of(new OrderItemRequestDto(item1.getId(), 1),
              new OrderItemRequestDto(item2.getId(), 2))
      );
      double expectTotalOrderPrice = 40.00;
      String valueAsString = objectMapper.writeValueAsString(dto);

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
        assertThat(item.UserId()).isEqualTo(USER_ID);
        assertThat(item.orderId()).isEqualTo(orderResult.orderId());
      });

      OrderItemResponseDto orderItemResponseDtoOne = orderResult.items().get(0);
      assertThat(orderItemResponseDtoOne.productName()).isEqualTo(item1.getName());
      assertThat(orderItemResponseDtoOne.basePrice())
          .isEqualByComparingTo(item1.getBasePrice().toString());
      assertThat(orderItemResponseDtoOne.priceApplied())
          .isEqualByComparingTo(item1.getFinalPrice().toString());
      assertThat(orderItemResponseDtoOne.quantity()).isEqualTo(1);
      assertThat(orderItemResponseDtoOne.totalPrice())
          .isEqualByComparingTo(item1.getFinalPrice().toString());

      OrderItemResponseDto orderItemResponseDtoTwo = orderResult.items().get(1);
      System.out.println(orderItemResponseDtoOne.basePrice());
      System.out.println(orderItemResponseDtoOne.basePrice().scale());
      assertThat(orderItemResponseDtoTwo.productName()).isEqualTo(item2.getName());
      assertThat(orderItemResponseDtoTwo.basePrice())
          .isEqualByComparingTo(item2.getBasePrice().toString());
      assertThat(orderItemResponseDtoTwo.priceApplied())
          .isEqualByComparingTo(item2.getFinalPrice().toString());
      assertThat(orderItemResponseDtoTwo.quantity()).isEqualTo(2);
      assertThat(orderItemResponseDtoTwo.totalPrice())
          .isEqualByComparingTo(item2.getFinalPrice().multiply(BigDecimal.valueOf(2)).toString());
    }

    @Test
    @DisplayName("User can get one of his existing order successfully")
    void getById_userCanGetOneOfHisOrderSuccessfully() throws Exception {
      Order orderRef = orderRepository.getReferenceById(1L);
      orderRef.setUser(getUserById(USER_ID));
      Order order = orderRepository.save(orderRef);
      mockMvc.perform(get(ORDER_BASE_URL + "/{id}", order.getId())
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(order.getId()))
          .andExpect(jsonPath("$.status").value(OrderStatus.DELIVERED.name()))
          .andExpect(jsonPath("$.items").isArray())
          .andExpect(jsonPath("$.items.size()").value(order.getOrderItems().size()))
          .andExpect(jsonPath("$.total").value(order.getTotal().doubleValue()));
    }

    @Test
    @DisplayName("Admin can get an existing order  by id successfully")
    void admin_canGetOrderByIdSuccessfully() throws Exception {
      String adminToken = performLogin(adminLogin);
      mockMvc.perform(get(ORDER_BASE_URL + "/{id}", 1L)
              .header("Authorization", adminToken))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User can get all his existing order successfully")
    void getAll_userCanGetAllHisOrdersSuccessfully() throws Exception {
      User user = getUserById(USER_ID);
      List<Order> orders = orderRepository.findByUserId(user.getId());
      mockMvc.perform(get(ORDER_BASE_URL)
              .header("Authorization", userToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", Matchers.hasSize(orders.size())))
          .andExpect(jsonPath("$[0].userId").value(user.getId().toString()))
          .andExpect(jsonPath("$[0].items.size()")
              .value(orders.get(0).getOrderItems().size()))
          .andExpect(jsonPath("$[0].total").value(orders.get(0).getTotal().doubleValue()))
          .andExpect(jsonPath("$[1].userId").value(user.getId().toString()))
          .andExpect(jsonPath("$[1].items.size()").value(orders.get(1)
              .getOrderItems().size()))
          .andExpect(jsonPath("$[1].total")
              .value(orders.get(1).getTotal().doubleValue()));
    }

  }

  @Nested
  @DisplayName("Exception Path")
  class OrderControllerExceptionPath {

    @Test
    @DisplayName("Create order returns 403 when no auth token is provided")
    void create_returns403_whenNoAuthTokenIsProvided() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(MockOrder.orderRequestDto());
      mockMvc.perform(post(ORDER_BASE_URL)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Create order returns 403 when token is invalid")
    void create_returns403_whenAuthTokenIsInvalid() throws Exception {
      String valueAsString = objectMapper.writeValueAsString(MockOrder.orderRequestDto());
      mockMvc.perform(post(ORDER_BASE_URL)
              .header("Authorization", "sdd88we5")
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Create order returns 404 with a negative quantity")
    void create_returns404_withANegativeQuantity() throws Exception {
      OrderRequestDto dto = new OrderRequestDto(
          List.of(new OrderItemRequestDto(4L, -1)));
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(post(ORDER_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create order returns 404 when buying quantity is greater than stock quantity")
    void create_returns404_whenBuyingQuantityIsGreaterThanStockQuantity() throws Exception {
      OrderRequestDto dto = new OrderRequestDto(
          List.of(new OrderItemRequestDto(1L, 9999999)));
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(post(ORDER_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create order returns 404 when try to buy a unactive itemWithId")
    void create_returns404_whenTryToBuyAUnactiveItem() throws Exception {
      Item itemRef = itemRepository.getReferenceById(1L);
      itemRef.setActive(false);
      Item testItem = itemRepository.save(itemRef);
      OrderRequestDto dto = new OrderRequestDto(
          List.of(new OrderItemRequestDto(testItem.getId(), 1)));
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(post(ORDER_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create order returns 404 with a null quantity")
    void create_returns404_withANullQuantity() throws Exception {
      OrderRequestDto dto = new OrderRequestDto(
          List.of(new OrderItemRequestDto(4L, null)));
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(post(ORDER_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create order returns 400 with a invalid id")
    void create_returns400_withAInvalidId() throws Exception {
      OrderRequestDto dto = new OrderRequestDto(
          List.of(new OrderItemRequestDto(999999L, 1)));
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(post(ORDER_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Create order returns 404 with a null id")
    void create_returns404_withANullId() throws Exception {
      OrderRequestDto dto = new OrderRequestDto(
          List.of(new OrderItemRequestDto(null, 2)));
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(post(ORDER_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create order returns 404 with a empty item list")
    void create_returns404_withAEmptyItemList() throws Exception {
      OrderRequestDto dto = new OrderRequestDto(List.of());
      String valueAsString = objectMapper.writeValueAsString(dto);
      mockMvc.perform(post(ORDER_BASE_URL)
              .header("Authorization", userToken)
              .contentType(MediaType.APPLICATION_JSON)
              .content(valueAsString))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get order by Id returns 403 when no auth token is provided")
    void getById_returns403_whenNoAuthTokenIsProvided() throws Exception {
      mockMvc.perform(get(ORDER_BASE_URL + "{id}", 1))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get order by id returns 403 when token is invalid")
    void getById_returns403_whenAuthTokenIsInvalid() throws Exception {
      mockMvc.perform(get(ORDER_BASE_URL + "{id}", 1)
              .header("Authorization", "sdd88we5"))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get order by id returns 403 when userWithId is not owner of the order")
    void getById_returns403_whenUserIsNotOwnerOfTheOrder() throws Exception {
      mockMvc.perform(get(ORDER_BASE_URL + "{id}", 2))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get order by id returns 400 with a invalid id")
    void getById_returns403_withAInvalidId() throws Exception {
      mockMvc.perform(get(ORDER_BASE_URL + "{id}", 99999999)
              .header("Authorization", userToken))
          .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get all orders returns 403 when no auth token is provided")
    void getAll_returns403_whenNoAuthTokenIsProvided() throws Exception {
      mockMvc.perform(get(ORDER_BASE_URL))
          .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get all orders returns 403 when token is invalid")
    void getAll_returns403_whenAuthTokenIsInvalid() throws Exception {
      mockMvc.perform(get(ORDER_BASE_URL)
              .header("Authorization", "sdd88we5"))
          .andExpect(status().isForbidden());
    }

  }

//  private Item saveItem(Item newItem) {
//    Blacksmith blacksmith = findBlacksmithById(newItem.getBlacksmithIdSnapshot());
//    newItem.setCraftedBy(blacksmith);
//    newItem.setBlacksmithIdSnapshot(blacksmith.getId());
//    newItem.setBlacksmithNameSnapshot(blacksmith.getName());
//    return itemRepository.save(newItem);
//  }
//
//  private Blacksmith findBlacksmithById(Long id) {
//    return blacksmithRepository.findById(id)
//        .orElseThrow(() -> new IllegalStateException("Blacksmith not found in test DB"));
//  }
}
