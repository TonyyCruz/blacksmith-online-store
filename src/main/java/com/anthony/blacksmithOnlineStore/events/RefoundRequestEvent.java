package com.anthony.blacksmithOnlineStore.events;

import java.util.List;

import com.anthony.blacksmithOnlineStore.entity.OrderItem;

public record RefoundRequestEvent(long orderId, List<OrderItem> orderItems) {

}
