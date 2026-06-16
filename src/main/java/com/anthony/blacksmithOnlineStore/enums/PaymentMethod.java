package com.anthony.blacksmithOnlineStore.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    DEBIT_CARD("Debit card"),
    CREDIT_CARD("Credit card"),
    PIX("Pix"),
    BANK_SLIP("BankSlip");

    private final String status;

    PaymentMethod(String status) {
        this.status = status;
    }
}
