package com.anthony.blacksmithOnlineStore.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    DEBIT("Debit"),
    CREDIT("Credit");

    private final String status;

    PaymentMethod(String status) {
        this.status = status;
    }
}
