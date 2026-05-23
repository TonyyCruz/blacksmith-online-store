package com.anthony.blacksmithOnlineStore.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
  PENDING("Pending"),
  PAYMENT_APPROVED("Payment Approved"),
  SEPARATING("Separating"),
  DISPATCHED("Dispatched"),
  IN_TRANSIT("In Transit"),
  OUT_FOR_DELIVERY("Out for Delivery"),
  DELIVERED("Delivered"),
  REFUND_PENDING("Refound Pending"),
  REFUNDED("Refunded"),
  DELIVERY_FAILED("Delivery Failed"),
  PAYMENT_REJECTED("Payment Rejected"),
  CANCELLED("Cancelled");

  private final String status;

  OrderStatus(String status) {
    this.status = status;
  }

  public boolean isFinalState() {
    return switch (this) {
      case PENDING, PAYMENT_REJECTED -> false;
      default -> true;
    };
  }

  public boolean canChangeTo(OrderStatus nextStatus) {
    return switch (this) {
      case PENDING -> nextStatus == PAYMENT_APPROVED
          || nextStatus == PAYMENT_REJECTED
          || nextStatus == CANCELLED;
      case PAYMENT_APPROVED -> nextStatus == SEPARATING
          || nextStatus == REFUND_PENDING;
      case SEPARATING -> nextStatus == DISPATCHED
          || nextStatus == REFUND_PENDING;
      case DISPATCHED -> nextStatus == IN_TRANSIT
          || nextStatus == DELIVERY_FAILED;
      case IN_TRANSIT -> nextStatus == OUT_FOR_DELIVERY
          || nextStatus == DELIVERY_FAILED;
      case OUT_FOR_DELIVERY -> nextStatus == DELIVERED
          || nextStatus == DELIVERY_FAILED;
      case DELIVERY_FAILED -> nextStatus == IN_TRANSIT
          || nextStatus == REFUND_PENDING;
      case DELIVERED -> nextStatus == REFUND_PENDING;
      case REFUND_PENDING -> nextStatus == REFUNDED;
      case PAYMENT_REJECTED -> nextStatus == CANCELLED;
      case CANCELLED, REFUNDED -> false;
    };
  }

  public boolean isPaid() {
    return switch (this) {
      case PAYMENT_APPROVED,
           SEPARATING,
           DISPATCHED,
           IN_TRANSIT,
           OUT_FOR_DELIVERY,
           DELIVERED,
           REFUND_PENDING-> true;
      default -> false;
    };
  }

  @Override
  public String toString() {
    return status;
  }
}
