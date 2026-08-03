package com.anthony.blacksmithOnlineStore.controller.dto.item;

import java.math.BigDecimal;

import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(toBuilder = true)
public record ItemRequestDto(
  @Schema(description = "The item name", example = "Spark")
  @Size(min = 2, message = "Name must have at lest 2 characters") 
  String name,
  @Schema(description = "The Item material", example = "IRON")
  @NotNull(message = "Material must not be null") 
  Material material,
  @Schema(description = "Base item damage (integer)", example = "1")
  @Min(value = 0, message = "Base damage cannot be less than 0") 
  Integer baseDamage,
  @Schema(description = "Base item defense (integer)", example = "0")
  @Min(value = 0, message = "Base defense cannot be less than 0") 
  Integer baseDefense,
  @Schema(description = "Base item price (decimal)", example = "84.50")
  @Min(value = 0, message = "Base price must not be a negative number") 
  BigDecimal basePrice,
  @Schema(description = "Final item price (decimal)", example = "82.00")
  @Min(value = 0, message = "Final price must not be a negative number") 
  BigDecimal finalPrice,
  @Schema(description = "Item description", 
    example = "Thirsty for blood, this dagger is excellent for causing hemorrhage.")
  @Size(min = 10, message = "Description must have at lest 10 characters") 
  String description,
  @Schema(description = "Item weight (decimal)", example = "3.40")
  @Min(value = 0, message = "Weight must not be a negative number") 
  Double weight,
  @Schema(description = "Item stock (integer)", example = "5")
  @Min(value = 0, message = "Stock must not be a negative number") 
  Integer stock,
  @Schema(description = "The item type", example = "DAGGER")
  @NotNull(message = "Type must not be null") 
  Type type,
  @Schema(description = "The item rariry", example = "EPIC")
  @NotNull(message = "Rarity must not be null") 
  Rarity rarity,
  @Schema(description = "The blacksmith id (integer)", example = "1")
  @NotNull(message = "Blacksmith must not be null") 
  Long blacksmithId,
  @Schema(description = "If the item is active or not. Only admin can access unactive items"
      + "Optional. If omitted, the current value is preserved", 
        example = "true")
  boolean active
) {

  public static Item toEntity(ItemRequestDto dto) {
    Item item = new Item();
    item.setName(dto.name());
    item.setMaterial(dto.material());
    item.setBaseDamage(dto.baseDamage());
    item.setBaseDefense(dto.baseDefense());
    item.setBasePrice(dto.basePrice());
    item.setFinalPrice(dto.finalPrice());
    item.setHasDiscount(dto.finalPrice().compareTo(dto.basePrice()) < 0);
    item.setDescription(dto.description());
    item.setWeight(dto.weight());
    item.setStock(dto.stock());
    item.setType(dto.type());
    item.setRarity(dto.rarity());
    item.setActive(dto.active());
    return item;
  }
}
