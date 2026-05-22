package com.anthony.blacksmithOnlineStore.controller.dto.item;

import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import com.anthony.blacksmithOnlineStore.validations.item.NullOrRange;
import com.anthony.blacksmithOnlineStore.validations.item.NullOrSize;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Builder;

@Builder(toBuilder = true)
public record ItemPatchUpdateDto(
    @NullOrSize(min = 2, message = "Name must have at lest 2 characters") String name,
    Material material,
    @NullOrRange(min = 1, message = "Base damage must be at lest 1") Integer baseDamage,
    @Positive(message = "Base defense must not be negative") Integer baseDefense,
    @Positive(message = "Base price must not be a negative number") BigDecimal basePrice,
    @Positive(message = "Final price must not be a negative number") BigDecimal finalPrice,
    @NullOrSize(min = 10, message = "Description must have at lest 10 characters") String description,
    @Positive(message = "Weight price must not be a negative number") Float weight,
    @Positive(message = "Stock price must not be a negative number") Integer stock,
    Type type,
    Rarity rarity,
    Long blacksmithId,
    Boolean active
) {

}
