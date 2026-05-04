package com.anthony.blacksmithOnlineStore.controler.dto.blacksmith;

import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import jakarta.validation.constraints.Size;

public record BlacksmithRequestDto(
    @Size(min = 2, message = "Name must have at lest 2 characters") String name,
    @Size(min = 10, message = "Description must have at lest 10 characters") String description) {

  public static Blacksmith toEntity(BlacksmithRequestDto dto) {
    Blacksmith blacksmith = new Blacksmith();
    blacksmith.setName(dto.name);
    blacksmith.setDescription(dto.description);
    return blacksmith;
  }
}
