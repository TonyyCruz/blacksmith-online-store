package com.anthony.blacksmithOnlineStore.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "ratings")
@Builder(toBuilder = true)
public class Rating {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private UUID reviewerUserId;
  @Column(nullable = false)
  private Long reviewedItemId;
  @Column(nullable = false)
  private Long reviewedBlacksmithId;
  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_item_id", unique = true)
  private OrderItem orderItem;
  @Column(nullable = false)
  private String reviewerUsername;
  @Column(nullable = false)
  private Integer ratingValue;
  @Column(columnDefinition = "TEXT")
  private String review;
  @CreationTimestamp
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt;
  @UpdateTimestamp
  @Setter(AccessLevel.NONE)
  private LocalDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Rating rating = (Rating) o;
    return Objects.equals(id, rating.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Rating{" +
        "orderId=" + id +
        ", reviewerUserId=" + reviewerUserId +
        ", reviewerUsername=" + reviewerUsername +
        ", orderItem=" + (orderItem == null ? null : orderItem.getId()) +
        ", ratingValue=" + ratingValue +
        ", comment='" + review + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
