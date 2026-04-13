package com.anthony.blacksmithOnlineStore.controler.dto.item;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ItemRequestDto(
    String name,
    Material material,
    Integer baseDamage,
    Integer baseDefense,
    BigDecimal basePrice,
    BigDecimal finalPrice,
    String description,
    Float weight,
    Integer stock,
    Type type,
    Rarity rarity,
    Long blacksmithId,
    boolean active
) {

  public static Item toEntity(ItemRequestDto dto) {
    Item item = new Item();
    item.setMaterial(dto.material());
    item.setBaseDamage(dto.baseDamage());
    item.setBaseDefense(dto.baseDefense());
    item.setName(dto.name());
    item.setBasePrice(dto.basePrice());
    item.setFinalPrice(dto.finalPrice());
    item.setDescription(dto.description());
    item.setWeight(dto.weight());
    item.setStock(dto.stock());
    item.setType(dto.type());
    item.setRarity(dto.rarity());
    item.setActive(dto.active());
    return item;
  }
}
