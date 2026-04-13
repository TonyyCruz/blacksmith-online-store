package com.anthony.blacksmithOnlineStore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blacksmiths")
public class Blacksmith {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;
  @Column(nullable = false)
  @OneToMany(mappedBy = "craftedBy")
  private final List<Item> craftedItems = new ArrayList<>();
  @Column(nullable = false)
  @Setter(AccessLevel.NONE)
  private long totalRatingsSum = 0;
  @Setter(AccessLevel.NONE)
  @Column(nullable = false)
  private Integer ratingCount = 0;
  @Column(precision = 2, scale = 1)
  @Setter(AccessLevel.NONE)
  private BigDecimal ratingAverage;
  @Version
  private Long version;

  public void addRating(Integer newRating) {
    ratingCount++;
    totalRatingsSum += newRating;
    ratingAverage = BigDecimal
        .valueOf(totalRatingsSum)
        .divide(BigDecimal.valueOf(ratingCount), 1, RoundingMode.HALF_UP);
  }

  public void addCraftedItem(Item item) {
    craftedItems.add(item);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Blacksmith that = (Blacksmith) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "Blacksmith{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", ratingAverage=" + ratingAverage +
        ", ratingCount=" + ratingCount +
        '}';
  }
}
