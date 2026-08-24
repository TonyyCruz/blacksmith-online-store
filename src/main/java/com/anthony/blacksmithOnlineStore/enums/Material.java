package com.anthony.blacksmithOnlineStore.enums;

import lombok.Getter;

@Getter
public enum Material {
  COPPER("Copper"),
  IRON("Iron"),
  STEEL("Steel"),
  MITHRIL("Mithril"),
  ADAMANTIUM("Adamantium"),
  OBSIDIAN("Obsidian");

  private final String materialName;

  Material(String materialName) {
    this.materialName = materialName;
  }
}
