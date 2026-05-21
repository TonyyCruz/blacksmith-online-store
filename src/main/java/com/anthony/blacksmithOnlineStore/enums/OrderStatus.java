package com.anthony.blacksmithOnlineStore.enums;

public enum OrderStatus {
  PENDING("Pending"),
  PAYMENT_APPROVED("Payment Approved"),
  SEPARATING("Separating"),
  DISPATCHED("Dispatched"),
  IN_TRANSIT("In Transit"),
  OUT_FOR_DELIVERY("Out for Delivery"),
  DELIVERED("Delivered"),
  PAYMENT_REJECTED("Payment Rejected"),
  CANCELLED("Cancelled"),
  REFUNDED("Refunded"),
  DELIVERY_FAILED("Delivery Failed");

  private final String status;

  OrderStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public boolean isFinalState() {
    return switch (this) {
      case DELIVERED, CANCELLED, REFUNDED -> true;
      default -> false;
    };
  }

  public boolean canChangeTo(OrderStatus nextStatus) {
    return switch (this) {
      case PENDING -> nextStatus == PAYMENT_APPROVED
          || nextStatus == PAYMENT_REJECTED
          || nextStatus == CANCELLED;
      case PAYMENT_APPROVED -> nextStatus == SEPARATING
          || nextStatus == CANCELLED;
      case SEPARATING -> nextStatus == DISPATCHED
          || nextStatus == CANCELLED;
      case DISPATCHED -> nextStatus == IN_TRANSIT
          || nextStatus == DELIVERY_FAILED;
      case IN_TRANSIT -> nextStatus == OUT_FOR_DELIVERY
          || nextStatus == DELIVERY_FAILED;
      case OUT_FOR_DELIVERY -> nextStatus == DELIVERED
          || nextStatus == DELIVERY_FAILED;
      case PAYMENT_REJECTED, DELIVERY_FAILED -> nextStatus == CANCELLED;
      case DELIVERED, CANCELLED, REFUNDED -> false;
    };
  }

  @Override
  public String toString() {
    return status;
  }
}
