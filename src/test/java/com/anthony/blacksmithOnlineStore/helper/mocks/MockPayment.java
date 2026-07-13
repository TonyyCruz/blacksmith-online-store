package com.anthony.blacksmithOnlineStore.helper.mocks;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.BankSlipDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.CreditDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.DebitDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PixDTO;
import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;
import java.math.BigDecimal;

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
}
