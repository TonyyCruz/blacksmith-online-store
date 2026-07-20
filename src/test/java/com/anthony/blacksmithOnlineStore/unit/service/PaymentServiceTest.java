package com.anthony.blacksmithOnlineStore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.BankSlipDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.CreditDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.DebitDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.PixDTO;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import com.anthony.blacksmithOnlineStore.events.OrderPaidEvent;
import com.anthony.blacksmithOnlineStore.exceptions.InvalidOrderStatusException;
import com.anthony.blacksmithOnlineStore.exceptions.PaymentException;
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
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#payable")
    @DisplayName("Should can set the order status to paid when approved")
    void createPayment_shouldSetOrderStatusToPaid_whenApproved(OrderStatus status) {
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
      verify(eventPublisher, times(paymentDtos.size())).publishEvent(any(OrderPaidEvent.class));
    }

    @Test
    @DisplayName("Should set the order status to payment rejected when not approved")
    void createPayment_shouldSetOrderStatusToPaymentRejected_whenNotApproved() {
      List<PaymentCreateDto> paymentDtos = List.of(
          MockPayment.creditCard().toBuilder().credit(new CreditDto(false)).build(),
          MockPayment.debitCard().toBuilder().debit(new DebitDto(false)).build(),
          MockPayment.bankSlip().toBuilder().bankSlip(new BankSlipDto(false)).build(),
          MockPayment.pix().toBuilder().pix(new PixDTO(false)).build()
      );

      for (PaymentCreateDto dto : paymentDtos) {
        Order order = MockOrder.orderWithItems().toBuilder().status(OrderStatus.PENDING).build();
        dto = dto.toBuilder().amount(order.getTotal()).build();

        when(orderService.getEntityById(order.getId())).thenReturn(order);
        when(paymentFactory.getProcessor(dto.method())).thenReturn(mockPaymentProcessor(dto));
        when(paymentRepository.save(any(Payment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentResponseDto payment = paymentService.createPayment(order.getId(), dto);

        assertEquals(order.getId(), payment.orderId());
        assertEquals(dto.amount(), payment.amount());
        assertEquals(dto.method(), payment.method());
        assertEquals(PaymentStatus.REJECTED.name(), payment.status());
        verify(eventPublisher, times(0)).publishEvent(any(OrderPaidEvent.class));
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
      Order order = MockOrder.orderWithItems().toBuilder().status(status).build();
      PaymentCreateDto dto = MockPayment.creditCard().toBuilder().amount(order.getTotal()).build();

      when(orderService.getEntityById(order.getId())).thenReturn(order);
      when(paymentFactory.getProcessor(dto.method())).thenReturn(mockPaymentProcessor(dto));

      assertThrows(InvalidOrderStatusException.class, () -> {
        paymentService.createPayment(order.getId(), dto);
      });
      verify(eventPublisher, times(0)).publishEvent(any(OrderPaidEvent.class));
    }

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#payable")
    @DisplayName("Should throw an exception when the payment amount is lesser than order total")
    void createPayment_shouldThrownAnException_withPaymentLesserThanOrderAmount(OrderStatus status) {
      List<PaymentCreateDto> paymentDtos = List.of(
          MockPayment.creditCard(),
          MockPayment.debitCard(),
          MockPayment.bankSlip(),
          MockPayment.pix()
      );

      for (PaymentCreateDto dto : paymentDtos) {
        Order order = MockOrder.orderWithItems().toBuilder().status(status).build();
        dto = dto.toBuilder().amount(order.getTotal().min(BigDecimal.ONE)).build();

        when(orderService.getEntityById(order.getId())).thenReturn(order);

        PaymentCreateDto finalDto = dto;
        assertThrows(PaymentException.class, () -> {
          paymentService.createPayment(order.getId(), finalDto);
        });
        verify(eventPublisher, times(0)).publishEvent(any(OrderPaidEvent.class));
      }
    }

    @ParameterizedTest
    @MethodSource("com.anthony.blacksmithOnlineStore.unit.service.helper.OrderStatusHelper#payable")
    @DisplayName("Should throw an exception when the payment amount is greater than order total")
    void createPayment_shouldThrownAnException_withPaymentGreaterThanOrderAmount(OrderStatus status) {
      List<PaymentCreateDto> paymentDtos = List.of(
          MockPayment.creditCard(),
          MockPayment.debitCard(),
          MockPayment.bankSlip(),
          MockPayment.pix()
      );

      for (PaymentCreateDto dto : paymentDtos) {
        Order order = MockOrder.orderWithItems().toBuilder().status(status).build();
        dto = dto.toBuilder().amount(order.getTotal().add(BigDecimal.ONE)).build();

        when(orderService.getEntityById(order.getId())).thenReturn(order);

        PaymentCreateDto finalDto = dto;
        assertThrows(PaymentException.class, () -> {
          paymentService.createPayment(order.getId(), finalDto);
        });
        verify(eventPublisher, times(0)).publishEvent(any(OrderPaidEvent.class));
      }
    }

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
