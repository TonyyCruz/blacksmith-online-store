package com.anthony.blacksmithOnlineStore.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    APPROVED("Approved"),
    REJECTED("Rejected"),
    REFOUNDED("Refounded");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }
}
