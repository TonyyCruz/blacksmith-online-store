package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.CreditDto;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockOrder;
import com.anthony.blacksmithOnlineStore.helper.mocks.MockPayment;
import com.anthony.blacksmithOnlineStore.payment.BankSlipProcessor;
import com.anthony.blacksmithOnlineStore.payment.CreditCardProcessor;
import com.anthony.blacksmithOnlineStore.payment.DebitProcessor;
import com.anthony.blacksmithOnlineStore.payment.PaymentProcessor;
import com.anthony.blacksmithOnlineStore.payment.PaymentProcessorFactory;
import com.anthony.blacksmithOnlineStore.payment.PixProcessor;
import com.anthony.blacksmithOnlineStore.repository.PaymentRepository;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import com.anthony.blacksmithOnlineStore.service.PaymentService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
  @Mock
  PaymentProcessorFactory paymentFactory;
  @Mock
  private PaymentRepository paymentRepository;
  @Mock
  private ApplicationEventPublisher eventPublisher;
  @Mock
  private OrderService orderService;
  @InjectMocks
  PaymentService paymentService;

  @Nested
  @DisplayName("Happy Path")
  class PaymentServiceHappyPath {


    @ParameterizedTest
    @DisplayName("Should set the order status to paid")
    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#payable")
    void createPayment_shouldSetOrderStatusToPaid(OrderStatus status) {
      List<PaymentCreateDto> paymentDtos = List.of(
          MockPayment.creditCard().toBuilder().credit(new CreditDto(true)).build(),
          MockPayment.debitCard().toBuilder().credit(new CreditDto(true)).build(),
          MockPayment.bankSlip().toBuilder().credit(new CreditDto(true)).build(),
          MockPayment.pix().toBuilder().credit(new CreditDto(true)).build()
      );

      for (PaymentCreateDto dto : paymentDtos) {
        Order order = MockOrder.orderWithItems().toBuilder().status(status).build();
        dto = dto.toBuilder().amount(order.getTotal()).build();

        when(orderService.getEntityById(order.getId())).thenReturn(order);
        when(paymentFactory.getProcessor(dto.method())).thenReturn(mockPaymentProcessor(dto));
        when(paymentRepository.save(any(Payment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentResponseDto payment = paymentService.createPayment(order.getId(), dto);

        assertEquals(order.getId(), payment.orderId());
        assertEquals(dto.amount(), payment.amount());
        assertEquals(dto.method(), payment.method());
        assertEquals(PaymentStatus.APPROVED.name(), payment.status());
      }
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

    //    @Test
//    @DisplayName("Should thrown an exception trying pay an order that is not yours")
//    void orderPaid_shouldThrownAnException_tryingPayAnOrderThatIsNotYours() {
//      Order order = MockOrder.orderWithItems().toBuilder().user(user).build();
//
//      when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
//      when(authUser.getAuthenticatedId()).thenReturn(UUID.randomUUID());
//
//      assertThrows(ForbiddenOperationException.class,
//          () -> paymentService.orderConfirmed(order.getId()),
//          "Should thrown an exception trying cancel an unauthorized order");
//      verify(orderRepository, times(1)).findById(order.getId());
//      verify(authUser, times(1)).getAuthenticatedId();
//    }
  }

  private PaymentProcessor mockPaymentProcessor(PaymentCreateDto dto) {
    return switch (dto.method().name()) {
      case "CREDIT_CARD" -> new CreditCardProcessor();
      case "DEBIT_CARD" -> new DebitProcessor();
      case "BANK_SLIP" -> new BankSlipProcessor();
      case "PIX" -> new PixProcessor();
      default -> throw new IllegalStateException(
          "invalid payment method in DB: " + dto.method().name());
    };
  }
}
