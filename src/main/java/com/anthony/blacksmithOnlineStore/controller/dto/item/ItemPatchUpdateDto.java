package com.anthony.blacksmithOnlineStore.controller.dto.item;

import java.math.BigDecimal;

import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import com.anthony.blacksmithOnlineStore.validations.item.NullOrRange;
import com.anthony.blacksmithOnlineStore.validations.item.NullOrSize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder(toBuilder = true)
public record ItemPatchUpdateDto(
    @Schema(description = "Item name. Optional. If omitted, the current value is preserved",
      example = "Spark")
    @NullOrSize(min = 2, message = "Name must have at lest 2 characters")
    String name,
    @Schema(description = "Item material Optional. If omitted, the current value is preserved",
      example = "IRON")
    Material material,
    @Schema(description = "Base item damage (integer) Optional. If omitted, the current value is preserved",
      example = "1")
    @NullOrRange(min = 1, message = "Base damage must be at lest 1")
    Integer baseDamage,
    @Schema(description = "Base item defense (integer) Optional. If omitted, the current value is preserved",
      example = "0")
    @Min(value = 0, message = "Base defense must not be negative")
    Integer baseDefense,
    @Schema(description = "Base item price (decimal) Optional. If omitted, the current value is preserved",
      example = "84.50")
    @Min(value = 0, message = "Base price must not be a negative number")
    BigDecimal basePrice,
    @Schema(description = "Final item price (decimal) Optional. If omitted, the current value is preserved",
      example = "82.00")
    @Min(value = 0, message = "Final price must not be a negative number")
    BigDecimal finalPrice,
    @Schema(description = "Item description. Optional. If omitted, the current value is preserved",
      example = "Thirsty for blood, this dagger is excellent for causing hemorrhage.")
    @NullOrSize(min = 10, message = "Description must have at lest 10 characters")
    String description,
    @Schema(description = "Item weight (decimal) Optional. If omitted, the current value is preserved",
      example = "3.40")
    @Min(value = 0, message = "Weight must not be a negative number")
    Float weight,
    @Schema(description = "Item stock (integer) Optional. If omitted, the current value is preserved",
      example = "5")
    @Min(value = 0, message = "Stock must not be a negative number")
    Integer stock,
    @Schema(description = "The item type. Optional. If omitted, the current value is preserved",
      example = "DAGGER")
    Type type,
    @Schema(description = "The item rarity. Optional. If omitted, the current value is preserved",
      example = "EPIC")
    Rarity rarity,
    @Schema(description = "The blacksmith id (integer) Optional. If omitted, the current value is preserved",
      example = "1")
    @Min(value = 1, message = "Blacksmith id must be a positive number greater than 0")
    Long blacksmithId,
    @Schema(description = "If the item is active or not. Only admin can access unactive items"
      + "Optional. If omitted, the current value is preserved",
        example = "true")
    Boolean active
) {

}
