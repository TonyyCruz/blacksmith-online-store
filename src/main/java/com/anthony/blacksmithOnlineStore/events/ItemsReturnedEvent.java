package com.anthony.blacksmithOnlineStore.events;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import java.time.LocalDateTime;
import java.util.List;

public record ItemsReturnedEvent(List<OrderItem> orderItems, LocalDateTime returnedAt) {

}
