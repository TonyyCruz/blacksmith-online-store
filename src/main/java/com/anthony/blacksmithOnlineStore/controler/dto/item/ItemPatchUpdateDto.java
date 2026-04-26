package com.anthony.blacksmithOnlineStore.controler.dto.item;

import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ItemPatchUpdateDto(
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
    Boolean active
) {

}
