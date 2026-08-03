package com.anthony.blacksmithOnlineStore.controller.dto.blacksmith;

import com.anthony.blacksmithOnlineStore.entity.Blacksmith;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record BlacksmithResponseDto(
  @Schema(example = "1")
  Long id,
  @Schema(example = "Rauran the Sleeper")
  String name, 
  @Schema(example = "He sleeps for days, but when he wakes up, he works for a week.")
  String description, 
  @Schema(example = "3")
  Integer ratingCount,
  @Schema(example = "4.4")
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
