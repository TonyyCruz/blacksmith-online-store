package com.anthony.blacksmithOnlineStore.entity;

import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  private Material material;
  private Integer baseDamage;
  private Integer baseDefense;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private BigDecimal basePrice;
  @Column(nullable = false)
  private BigDecimal finalPrice;
  @Column(nullable = false)
  private boolean hasDiscount = false;
  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;
  @Column(nullable = false)
  private Float weight;
  @Column(nullable = false)
  private Integer stock;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Type type;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Rarity rarity;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "blacksmith_id")
  private Blacksmith craftedBy;
  @Column(nullable = false)
  @Setter(AccessLevel.NONE)
  private long sold = 0;
  @Column(nullable = false)
  @Setter(AccessLevel.NONE)
  private long totalRatingsSum = 0;
  @Setter(AccessLevel.NONE)
  @Column(nullable = false)
  private Integer ratingCount = 0;
  @Column(precision = 2, scale = 1)
  @Setter(AccessLevel.NONE)
  private BigDecimal ratingAverage;
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  @Setter(AccessLevel.NONE)
  private LocalDateTime createdAt;
  @UpdateTimestamp
  @Setter(AccessLevel.NONE)
  private LocalDateTime updatedAt;
  @Column(nullable = false)
  private boolean active = true;
  @Version
  private Long version;
  @Column(nullable = false, updatable = false)
  private String blacksmithNameSnapshot;
  @Column(nullable = false, updatable = false)
  private Long blacksmithIdSnapshot;

  public void addRating(Integer newRating) {
    ratingCount++;
    totalRatingsSum += newRating;
    ratingAverage = BigDecimal
        .valueOf(totalRatingsSum)
        .divide(BigDecimal.valueOf(ratingCount), 1, RoundingMode.HALF_UP);
  }

  public void addSoldQuantity(int quantity) {
    this.sold += quantity;
  }

  public void removeSoldQuantity(int quantity) {
    this.sold -= quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Item item = (Item) o;
    return Objects.equals(id, item.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

}
