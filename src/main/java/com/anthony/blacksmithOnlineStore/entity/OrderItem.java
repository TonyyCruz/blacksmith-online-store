package com.anthony.blacksmithOnlineStore.entity;

import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private Long itemId;
  @Column(nullable = false)
  private String itemName;
  @Column(nullable = false)
  private BigDecimal basePriceAtPurchase;
  @Column(nullable = false)
  private BigDecimal priceApplied;
  @Column(nullable = false)
  private Integer quantity;
  @Column(nullable = false)
  private BigDecimal totalPrice;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rating_id", unique = true)
  private Rating rating;
  private Integer ratingValue;
  @Column(nullable = false, updatable = false)
  private UUID userId;
  @Column(nullable = false, updatable = false)
  private Long blacksmithId;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @Column(nullable = false)
  private boolean reviewed = false;

  public void setId(Long id) {
    checkIfFinalized();
    this.id = id;
  }

  public void setItemId(Long itemId) {
    checkIfFinalized();
    this.itemId = itemId;
  }

  public void setItemName(String itemName) {
    checkIfFinalized();
    this.itemName = itemName;
  }

  public void setBasePriceAtPurchase(BigDecimal basePriceAtPurchase) {
    checkIfFinalized();
    this.basePriceAtPurchase = basePriceAtPurchase;
  }

  public void setPriceApplied(BigDecimal priceApplied) {
    checkIfFinalized();
    this.priceApplied = priceApplied;
  }

  public void setQuantity(Integer quantity) {
    checkIfFinalized();
    this.quantity = quantity;
  }

  public void calculateTotal() {
    if (priceApplied != null && quantity != null) {
      this.totalPrice = priceApplied.multiply(BigDecimal.valueOf(quantity));
    }
  }

  public void setOrder(Order order) {
    checkIfFinalized();
    this.order = order;
  }

  public void setRating(Rating newRating) {
    if (!(rating == null)) throw new ForbiddenOperationException("Rating cannot be changed.");
    rating = newRating;
    ratingValue = newRating.getRatingValue();
    reviewed = true;
  }

  public void setUserId(UUID userId) {
    checkIfFinalized();
    this.userId = userId;
  }

  public void setBlacksmithId(Long blacksmithId) {
    checkIfFinalized();
    this.blacksmithId = blacksmithId;
  }

  private void checkIfFinalized() {
    if (order != null && order.getStatus().isFinalState()) {
      throw new IllegalStateException("Item não pode ser alterado após finalização.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    OrderItem orderItem = (OrderItem) o;
    return Objects.equals(id, orderItem.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "OrderItem{" +
        "id=" + id +
        ", productId=" + itemId +
        ", productName='" + itemName + '\'' +
        ", unitPrice=" + basePriceAtPurchase +
        ", quantity=" + quantity +
        ", totalPrice=" + totalPrice +
        ", ratingValue=" + ratingValue +
        ", order=" + (order == null ? null : order.getId()) +
        ", userId=" + userId +
        '}';
  }
}
