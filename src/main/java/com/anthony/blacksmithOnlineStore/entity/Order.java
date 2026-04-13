package com.anthony.blacksmithOnlineStore.entity;

import com.anthony.blacksmithOnlineStore.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
  @CreationTimestamp
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt;
  @UpdateTimestamp
  @Setter(AccessLevel.NONE)
  private LocalDateTime updatedAt;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus status = OrderStatus.RECEIVED;
  @Setter(AccessLevel.NONE)
  @OneToMany(mappedBy = "order", cascade =  CascadeType.PERSIST)
  private final List<OrderItem> orderItems = new ArrayList<>();
  @Setter(AccessLevel.NONE)
  @Column(nullable = false)
  private BigDecimal total;

  public void addOrderItem(OrderItem item) {
    checkIfFinalized();
    orderItems.add(item);
  }

  public void removeOrderItem(OrderItem item) {
    checkIfFinalized();
    orderItems.remove(item);
  }

  public void recalculateTotal() {
    this.total = orderItems.stream()
        .map(OrderItem::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private void checkIfFinalized() {
    if (status.isFinalState()) {
      throw new IllegalStateException("Item não pode ser alterado após finalização.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(id, order.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", user=" + (user == null? null : user.getId()) +
        ", createdAt=" + createdAt +
        ", status=" + status +
        ", orderItems=" + orderItems +
        ", total=" + total +
        '}';
  }
}
