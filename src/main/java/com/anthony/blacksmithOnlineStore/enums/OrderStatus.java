package com.anthony.blacksmithOnlineStore.enums;

public enum OrderStatus {
  PENDING("Pending"),
  PROCESSING("Processing"),
  PAYMENT_APPROVED("Payment Approved"),
  PAYMENT_REJECTED("Payment Rejected"),
  SEPARATING("Separating"),
  DISPATCHED("Dispatched"),
  IN_TRANSIT("In Transit"),
  OUT_FOR_DELIVERY("Out for Delivery"),
  DELIVERED("Delivered"),
  CANCELLED("Cancelled"),
  RETURN_REQUESTED("Return Requested"),
  REFUNDED("Refunded"),
  AWAITING_STOCK("Awaiting Stock"),
  DELIVERY_FAILED("Delivery Failed"),
  SUSPENDED("Suspended");

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

  @Override
  public String toString() {
    return status;
  }
}
