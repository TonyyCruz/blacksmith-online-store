package com.anthony.blacksmithOnlineStore.controler.dto.blacksmith;

import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record BlacksmithRequestDto(
    @NotBlank @Length(min = 2) String name,
    @NotBlank @Length(min = 10) String description) {

  public static Blacksmith toEntity(BlacksmithRequestDto dto) {
    Blacksmith blacksmith = new Blacksmith();
    blacksmith.setName(dto.name);
    blacksmith.setDescription(dto.description);
    return blacksmith;
  }
}
