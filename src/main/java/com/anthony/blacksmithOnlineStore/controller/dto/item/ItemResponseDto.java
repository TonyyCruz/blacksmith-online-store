package com.anthony.blacksmithOnlineStore.controller.dto.item;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ItemResponseDto(
  @Schema(example = "22")
  Long id,
  @Schema(example = "Dragon hammer")
  String name,
  @Schema(example = "MITHRIL")
  Material material,
  @Schema(example = "243")
  Integer baseDamage,
  @Schema(example = "32")
  Integer baseDefense,
  @Schema(example = "1,432.20")
  BigDecimal basePrice,
  @Schema(example = "1,100.00")
  BigDecimal finalPrice,
  @Schema(example = "A powerful club forged from dragon bones")
  String description,
  @Schema(example = "94.20")
  Double weight,
  @Schema(example = "4")
  Integer stock,
  @Schema(example = "CLUB")
  Type type,
  @Schema(example = "LEGENDARY")
  Rarity rarity,
  @Schema(example = "2")
  Long blacksmithId,
  @Schema(example = "Tron Steel Forged")
  String blacksmithName,
  @Schema(example = "4.8")
  BigDecimal ratingAverage,
  @Schema(example = "12")
  Integer ratingCount,
  @Schema(example = "23")
  Long sold,
  @Schema(example = "true")
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
