package com.anthony.blacksmithOnlineStore.helper.mocks;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.BankSlipDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.CreditDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.DebitDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.methods.PixDTO;
import com.anthony.blacksmithOnlineStore.entity.Order;
import com.anthony.blacksmithOnlineStore.entity.Payment;
import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;
import com.anthony.blacksmithOnlineStore.enums.PaymentStatus;
import java.math.BigDecimal;
import java.util.UUID;

public class MockPayment {

  public static PaymentCreateDto creditCard() {
    return new PaymentCreateDto(
        PaymentMethod.CREDIT_CARD,
        BigDecimal.valueOf(100.00),
        null,
        new CreditDto(true),
        null,
        null);
  }

  public static PaymentCreateDto debitCard() {
    return new PaymentCreateDto(
        PaymentMethod.DEBIT_CARD,
        BigDecimal.valueOf(100.00),
        new DebitDto(true),
        null,
        null,
        null);
  }

  public static PaymentCreateDto pix() {
    return new PaymentCreateDto(
        PaymentMethod.PIX,
        BigDecimal.valueOf(100.00),
        null,
        null,
        new PixDTO(true),
        null);
  }

  public static PaymentCreateDto bankSlip() {
    return new PaymentCreateDto(
        PaymentMethod.BANK_SLIP,
        BigDecimal.valueOf(100.00),
        null,
        null,
        null,
        new BankSlipDto(true));
  }

  public static Payment createPayment(Order order, PaymentCreateDto dto) {
    Payment payment = PaymentCreateDto.toEntity(dto);
    payment.setTransactionId(UUID.randomUUID().toString());
    payment.setOrder(order);
    payment.setPaymentStatus(PaymentStatus.APPROVED);
    order.setStatus(OrderStatus.PAYMENT_APPROVED);
    return payment;
  }
  
  public static Payment payment() {
  	return createPayment(MockOrder.orderWithItems(), creditCard());
  }
}
