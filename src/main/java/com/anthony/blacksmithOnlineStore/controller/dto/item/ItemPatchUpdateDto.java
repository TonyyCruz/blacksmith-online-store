package com.anthony.blacksmithOnlineStore.controller.dto.item;

import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import com.anthony.blacksmithOnlineStore.validations.item.NullOrRange;
import com.anthony.blacksmithOnlineStore.validations.item.NullOrSize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Builder;

@Builder(toBuilder = true)
public record ItemPatchUpdateDto(
    @NullOrSize(min = 2, message = "Name must have at lest 2 characters") String name,
    Material material,
    @NullOrRange(min = 1, message = "Base damage must be at lest 1") Integer baseDamage,
    @Min(value = 1, message = "Base defense must not be negative") Integer baseDefense,
    @Min(value = 1, message = "Base price must not be a negative number") BigDecimal basePrice,
    @Min(value = 1, message = "Final price must not be a negative number") BigDecimal finalPrice,
    @NullOrSize(min = 10, message = "Description must have at lest 10 characters")
    String description,
    @Min(value = 1, message = "Weight price must not be a negative number") Float weight,
    @Min(value = 1, message = "Stock price must not be a negative number") Integer stock,
    @NotNull Type type,
    @NotNull Rarity rarity,
    @Min(value = 1, message = "Blacksmith id must be a positive number")
    @NotNull Long blacksmithId,
    @NotNull Boolean active
) {

}
