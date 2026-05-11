package com.anthony.blacksmithOnlineStore.controler.dto.item;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ItemResponseDto(
    Long id,
    Material material,
    Integer baseDamage,
    Integer baseDefense,
    String name,
    BigDecimal basePrice,
    BigDecimal finalPrice,
    String description,
    Float weight,
    Integer stock,
    Type type,
    Rarity rarity,
    Long blacksmithId,
    String blacksmithName,
    BigDecimal ratingAverage,
    Integer ratingCount,
    Long sold,
    boolean active
) {

  public static ItemResponseDto fromEntity(Item item) {
    return ItemResponseDto.builder()
        .id(item.getId())
        .material(item.getMaterial())
        .baseDamage(item.getBaseDamage())
        .baseDefense(item.getBaseDefense())
        .name(item.getName())
        .basePrice(item.getBasePrice())
        .finalPrice(item.getFinalPrice())
        .description(item.getDescription())
        .weight(item.getWeight())
        .stock(item.getStock())
        .type(item.getType())
        .rarity(item.getRarity())
        .blacksmithId(item.getBlacksmithIdSnapshot())
        .blacksmithName(item.getBlacksmithNameSnapshot())
        .ratingAverage(item.getRatingAverage())
        .ratingCount(item.getRatingCount())
        .active(item.isActive())
        .sold(item.getSold())
        .build();
  }
}
