package com.anthony.blacksmithOnlineStore.controller.dto.blacksmith;

import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import java.math.BigDecimal;

public record BlacksmithResponseDto(Long id, String name, String description, Integer ratingCount,
                                    BigDecimal ratingAverage) {

  public static BlacksmithResponseDto fromEntity(Blacksmith blacksmith) {
    return new BlacksmithResponseDto(
        blacksmith.getId(),
        blacksmith.getName(),
        blacksmith.getDescription(),
        blacksmith.getRatingCount(),
        blacksmith.getRatingAverage()
    );
  }
}
