package com.anthony.blacksmithOnlineStore.controler.dto.item;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Builder;

@Builder(toBuilder = true)
public record ItemRequestDto(
    @Size(min = 2, message = "Name must have at lest 2 characters") String name,
    @NotNull(message = "Material must not be null") Material material,
    @Positive(message = "Base damage cannot be less than 0") Integer baseDamage,
    @Positive(message = "Base defense cannot be less than 0") Integer baseDefense,
    @Positive(message = "Base price must not be a negative number") BigDecimal basePrice,
    @Positive(message = "Final price must not be a negative number") BigDecimal finalPrice,
    @Size(min = 10, message = "Description must have at lest 10 characters") String description,
    @Positive(message = "Weight price must not be a negative number") Float weight,
    @Positive(message = "Stock price must not be a negative number") Integer stock,
    @NotNull(message = "Type must not be null") Type type,
    @NotNull(message = "Rarity must not be null") Rarity rarity,
    @NotNull(message = "Blacksmith must not be null") Long blacksmithId,
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
