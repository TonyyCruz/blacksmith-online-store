package com.anthony.blacksmithOnlineStore.events;

import java.math.BigDecimal;

import com.anthony.blacksmithOnlineStore.enums.PaymentMethod;

public record PaymentRefusedEvent(Long OrderId, PaymentMethod method, BigDecimal amount) {

}
