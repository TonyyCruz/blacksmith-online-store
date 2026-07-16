package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderPaymentDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderResponseDto;
import com.anthony.blacksmithOnlineStore.controller.dto.orderItem.OrderItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.User;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.exceptions.DataModifyException;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidOrderStatusException;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentNotFoundException;
import com.anthony.blacksmithOnlineStore.exceptions.OrderNotFoundException;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockItem;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockOrder;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockOrderItem;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockUser;
import com.anthony.blacksmithOnlineStore.repository.OrderRepository;
import com.anthony.blacksmithOnlineStore.security.utils.AuthenticatedUserService;
import com.anthony.blacksmithOnlineStore.service.ItemService;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import com.anthony.blacksmithOnlineStore.service.SaleService;
import com.anthony.blacksmithOnlineStore.service.UserService;
import com.anthony.blacksmithOnlineStore.service.util.OrderItemFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
  @Mock
  private OrderRepository orderRepository;
  @Mock
  private SaleService saleService;
  @Mock
  private AuthenticatedUserService authUser;
  @InjectMocks
  OrderService orderService;
  private final Item targetItem = MockItem.itemWithId();
  private final User user = MockUser.userWithId();

  @Nested
  @DisplayName("Happy Path")
  class PaymentServiceHappyPath {


    @ParameterizedTest
    @DisplayName("Should set the order status to paid")
    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#payable")
    void createPayment_shouldSetOrderStatusToPaid(OrderStatus status) {

    }

  }

  @Nested
  @DisplayName("Exception Path")
  class PaymentServiceExceptionPath {

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#nonPayable")
    @DisplayName("Order paid should throw an exception when try change to uncorrected status")
    void createPayment_shouldThrownAnException_whenTryChangeToUncorrectedStatus(OrderStatus status) {

    }
  }

}
