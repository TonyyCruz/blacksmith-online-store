package com.anthony.blacksmithOnlineStore.helper.mocks;

import com.anthony.blacksmithOnlineStore.controler.dto.blacksmith.BlacksmithRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Blacksmith;

public class MockBlacksmith {

  public static Blacksmith blacksmith() {
    Blacksmith blacksmith = new Blacksmith();
    blacksmith.setId(1L);
    blacksmith.setName("John the Smith");
    blacksmith.setDescription("Experienced blacksmith specializing in medieval weapons.");
    return blacksmith;
  }

  public static Blacksmith clone(Blacksmith blacksmith) {
    Blacksmith clone = new Blacksmith();
    clone.setId(blacksmith.getId());
    clone.setName(blacksmith.getName());
    clone.setDescription(blacksmith.getDescription());
    return clone;
  }

  // ========== DTOs ==========
  public static BlacksmithRequestDto requestDto() {
    return new BlacksmithRequestDto(
        "John the Smith",
        "Experienced blacksmith specializing in medieval weapons.");
  }

  public static BlacksmithRequestDto requestDto(String name, String description) {
    return new BlacksmithRequestDto(
        name,
        description);
  }

}
