package com.anthony.blacksmithOnlineStore.enums;

import lombok.Getter;

@Getter
public enum Type {
  SHORT_SWORD("Short Sword"),
  LONG_SWORD("Long Sword"),
  BROADSWORD("Broadsword"),
  CLAYMORE("Claymore"),
  CUTLASS("Cutlass"),
  DAGGER("Dagger"),
  STILETTO("Stiletto"),
  KATAR("Katar"),
  HAND_AXE("Hand Axe"),
  BATTLE_AXE("Battle Axe"),
  DOUBLE_HEADED_AXE("Double-Headed Axe"),
  SPEAR("Spear"),
  HALBERD("Halberd"),
  PIKE("Pike"),
  GLAIVE("Glaive"),
  BOW("Bow"),
  SHORT_BOW("Short Bow"),
  LONG_BOW("Long Bow"),
  CROSSBOW("Crossbow"),
  CLUB("Club"),
  MACE("Mace"),
  WAR_HAMMER("War Hammer"),
  FLAIL("Flail"),
  SLING("Sling"),
  THROWING_AXE("Throwing Axe"),
  JAVELIN("Javelin"),
  TRIDENT("Trident");

  private final String weapon;

  Type(String displayName) {
    this.weapon = displayName;
  }
}
