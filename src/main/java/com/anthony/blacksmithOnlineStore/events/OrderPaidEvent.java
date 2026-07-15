package com.anthony.blacksmithOnlineStore.events;

import java.time.LocalDateTime;

public record OrderPaidEvent(Long orderId, LocalDateTime paidAt) {

}
