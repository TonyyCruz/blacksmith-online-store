package com.anthony.blacksmithOnlineStore.helper;

import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import java.util.Arrays;

public class OrderStatusHelper {

  public static OrderStatus[] cancelable() {
    return Arrays.stream(OrderStatus.values())
        .filter(OrderStatus::canBeCanceled)
        .toArray(OrderStatus[]::new);
  }

  public static OrderStatus[] nonCancelable() {
    return Arrays.stream(OrderStatus.values())
        .filter(status -> !status.canBeCanceled())
        .toArray(OrderStatus[]::new);
  }

  public static OrderStatus[] refundable() {
    return Arrays.stream(OrderStatus.values())
        .filter(OrderStatus::canBeRefunded)
        .toArray(OrderStatus[]::new);
  }

  public static OrderStatus[] nonRefundable() {
    return Arrays.stream(OrderStatus.values())
        .filter(status -> !status.canBeRefunded())
        .toArray(OrderStatus[]::new);
  }

  public static OrderStatus[] returnable() {
    return Arrays.stream(OrderStatus.values())
        .filter(OrderStatus::canBeReturned)
        .toArray(OrderStatus[]::new);
  }

  public static OrderStatus[] nonReturnable() {
    return Arrays.stream(OrderStatus.values())
        .filter(status -> !status.canBeReturned())
        .toArray(OrderStatus[]::new);
  }

  public static OrderStatus[] payable() {
    return new OrderStatus[]{ OrderStatus.PENDING, OrderStatus.PAYMENT_REJECTED };
  }

  public static OrderStatus[] nonPayable() {
    return Arrays.stream(OrderStatus.values())
        .filter(status -> !status.equals(OrderStatus.PENDING)
            && !status.equals(OrderStatus.PAYMENT_REJECTED))
        .toArray(OrderStatus[]::new);
  }

}
