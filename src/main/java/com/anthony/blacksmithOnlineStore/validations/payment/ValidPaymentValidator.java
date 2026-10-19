package com.anthony.blacksmithOnlineStore.validations.payment;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPaymentValidator implements ConstraintValidator<ValidPayment, PaymentCreateDto> {

	@Override
  public boolean isValid(PaymentCreateDto dto, ConstraintValidatorContext context) {
    if (dto == null || dto.method() == null) return false;
    return switch (dto.method()) {
    	case CREDIT_CARD -> isCreditValid(dto);
    	case DEBIT_CARD -> isDebitValid(dto);
    	case BANK_SLIP -> isBankSlipValid(dto);
    	case PIX -> isPixValid(dto);
    	default -> false;
    };
  }
	
	private boolean isCreditValid(PaymentCreateDto dto) {
		return dto.credit() != null 
				&& dto.debit() == null 
				&& dto.bankSlip() == null
				&& dto.pix() == null;
	}
	
	private boolean isDebitValid(PaymentCreateDto dto) {
		return dto.credit() == null 
				&& dto.debit() != null 
				&& dto.bankSlip() == null
				&& dto.pix() == null;
	}
	
	private boolean isBankSlipValid(PaymentCreateDto dto) {
		return dto.credit() == null 
				&& dto.debit() == null 
				&& dto.bankSlip() != null
				&& dto.pix() == null;
	}
	
	private boolean isPixValid(PaymentCreateDto dto) {
		return dto.credit() == null 
				&& dto.debit() == null 
				&& dto.bankSlip() == null
				&& dto.pix() != null;
	}
}
