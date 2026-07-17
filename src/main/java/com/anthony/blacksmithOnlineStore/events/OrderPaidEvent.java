package com.anthony.blacksmithOnlineStore.events;

import java.time.LocalDateTime;

public record OrderPaidEvent(long orderId, LocalDateTime paidAt) {

}
