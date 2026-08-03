package com.anthony.blacksmithOnlineStore.controller.dto.blacksmith;

import com.anthony.blacksmithOnlineStore.entity.Blacksmith;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BlacksmithRequestDto(
    @Schema(description = "The Blacksmith name", example = "Rauran the Sleeper")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, message = "Name must have at lest 2 characters") 
    String name,
    @Schema(description = "The Blacksmith description", 
      example = "He sleeps for days, but when he wakes up, he works for a week.")
    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, message = "Description must have at lest 10 characters") 
    String description) {

  public static Blacksmith toEntity(BlacksmithRequestDto dto) {
    Blacksmith blacksmith = new Blacksmith();
    blacksmith.setName(dto.name);
    blacksmith.setDescription(dto.description);
    return blacksmith;
  }
}
