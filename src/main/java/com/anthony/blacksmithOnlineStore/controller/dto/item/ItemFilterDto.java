package com.anthony.blacksmithOnlineStore.controller.dto.item;

import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import java.math.BigDecimal;

public record ItemFilterDto(
    String name,
    Material material,
    Integer minDamage,
    Integer maxDamage,
    Integer minDefense,
    Integer maxDefense,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    Float minWeight,
    Float maxWeight,
    Type type,
    Rarity rarity,
    Long blacksmithId,
    Boolean active
) {
    public ItemFilterDto withActiveTrue() {
      return new ItemFilterDto(name, material, minDamage, maxDamage, minDefense, maxDefense, minPrice, maxPrice,
          minWeight, maxWeight, type, rarity, blacksmithId, true);
    }
}
