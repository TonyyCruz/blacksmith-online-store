package com.anthony.blacksmithOnlineStore.controller.dto.item;

import java.math.BigDecimal;

import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record ItemFilterDto(
    @Schema(description = "Partial or full item name. Optional. If omitted, this filter will be ignored", 
      example = "blade")
    String name,
    @Schema(description = "Item material. Optional. If omitted, this filter will be ignored", 
      example = "COOPER")
    Material material,
    @Schema(description = "The minimum item damage (integer). Optional. If omitted, this filter will be ignored",
      example = "1")
    @Min(value = 0, message = "Min damage cannot be less than 0")
    Integer minDamage,
    @Schema(description = "The maximum item damage (integer). Optional. If omitted, this filter will be ignored", 
      example = "100")
    @Min(value = 0, message = "Max damage cannot be less than 0")
    Integer maxDamage,
    @Schema(description = "The minimum item defense (integer). Optional. If omitted, this filter will be ignored", 
      example = "0")
    @Min(value = 0, message = "Min defense cannot be less than 0")
    Integer minDefense,
    @Schema(description = "The maximum item defense (integer). Optional. If omitted, this filter will be ignored", 
      example = "80")
    @Min(value = 0, message = "Max defense cannot be less than 0")
    Integer maxDefense,
    @Schema(description = "The minimum item price (decimal). Optional. If omitted, this filter will be ignored", 
      example = "5.00")
    @Min(value = 0, message = "Min price must not be a negative number")
    BigDecimal minPrice,
    @Schema(description = "The maximum item price (decimal). Optional. If omitted, this filter will be ignored", 
      example = "120.00")
    @Min(value = 0, message = "Max price must not be a negative number")
    BigDecimal maxPrice,
    @Schema(description = "The minimum item weight (decimal). Optional. If omitted, this filter will be ignored", 
      example = "1.00")
    @Min(value = 0, message = "Min weight must not be a negative number")
    Double minWeight,
    @Schema(description = "The maximum item weight (decimal). Optional. If omitted, this filter will be ignored", 
      example = "200.00")
    @Min(value = 0, message = "Max weight must not be a negative number")
    Double maxWeight,
    @Schema(description = "The item type. Optional. If omitted, this filter will be ignored", 
      example = "SHORT_SWORD")
    Type type,
    @Schema(description = "The item rariry. Optional. If omitted, this filter will be ignored", 
      example = "UNCOMMON")
    Rarity rarity,
    @Schema(description = "The blacksmith id (integer). Optional. If omitted, this filter will be ignored", 
      example = "1")
    @Min(value = 1, message = "Blacksmith id must be a positive number greater than 0")
    Long blacksmithId,
    @Schema(description = "Only admin can set active false, user will fing ever activated items."
      + "Optional. If omitted, this filter will be ignored", 
        example = "true")
    Boolean active
) {
    public ItemFilterDto withActiveTrue() {
      return new ItemFilterDto(name, material, minDamage, maxDamage, minDefense, maxDefense, minPrice, maxPrice,
          minWeight, maxWeight, type, rarity, blacksmithId, true);
    }
}
