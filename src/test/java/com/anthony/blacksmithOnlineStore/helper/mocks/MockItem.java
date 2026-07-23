package com.anthony.blacksmithOnlineStore.helper.mocks;

import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockItem {

  public static Item item(Long id) {
    return Item.builder()
        .id(id)
        .material(Material.ADAMANTIUM)
        .baseDamage(1000)
        .baseDefense(500)
        .name("Avalon blade")
        .basePrice(BigDecimal.valueOf(9999.99))
        .finalPrice(BigDecimal.valueOf(9999.99))
        .hasDiscount(false)
        .description("The sword of dreams")
        .weight(35.0d)
        .stock(1)
        .type(Type.LONG_SWORD)
        .rarity(Rarity.LEGENDARY)
        .craftedBy(MockBlacksmith.blacksmith())
        .blacksmithIdSnapshot(MockBlacksmith.blacksmith().getId())
        .blacksmithNameSnapshot(MockBlacksmith.blacksmith().getName())
        .ratingCount(0)
        .build();
  }

  public static Item item() {
    return item(999L);
  }

  public static Item newItem() {
    BigDecimal basePrice = BigDecimal.valueOf(Math.floor(Math.random() * 1000 + 11));
    BigDecimal dif = ((Math.random() > 0.5) ? BigDecimal.valueOf(10) : BigDecimal.ZERO);
    BigDecimal finalPrice = basePrice.subtract(dif);
    return Item.builder()
        .material(Material.ADAMANTIUM)
        .baseDamage(1000)
        .baseDefense(500)
        .name("Avalon blade%f".formatted(Math.random()))
        .basePrice(basePrice)
        .finalPrice(finalPrice)
        .hasDiscount(false)
        .description("The sword of dreams%f".formatted(Math.random()))
        .weight(Math.floor(Math.random() * 100 + 1))
        .stock(1)
        .type(Type.LONG_SWORD)
        .rarity(Rarity.LEGENDARY)
        .craftedBy(MockBlacksmith.blacksmith())
        .blacksmithIdSnapshot(MockBlacksmith.blacksmith().getId())
        .blacksmithNameSnapshot(MockBlacksmith.blacksmith().getName())
        .ratingCount(0)
        .hasDiscount(dif.compareTo(BigDecimal.ZERO) > 0)
        .build();
  }

  public static List<Item> newItems() {
    Item item1 = newItem();
    Item item2 = newItem().toBuilder()
        .stock(3)
        .material(Material.MITHRIL)
        .type(Type.BATTLE_AXE)
        .rarity(Rarity.DIVINE)
        .baseDamage(322)
        .baseDefense(100)
        .build();
        Item item3 = newItem().toBuilder()
        .stock(102)
        .material(Material.IRON)
        .type(Type.DAGGER)
        .rarity(Rarity.COMMON)
        .baseDamage(9)
        .baseDefense(1)
        .build();
        return new ArrayList<Item>(List.of(item1, item2, item3));
  }

  public static List<Item> items() {
    List<Item> items = newItems();
    Long fakeId = 1L;
    for (Item itm : items) {
      itm.setId(fakeId);
      fakeId++;
    }
    return items;
  }

  // ========== DTOs ==========

  public static ItemRequestDto itemRequestDto() {
    return ItemRequestDto.builder()
        .material(Material.STEEL)
        .baseDamage(35)
        .baseDefense(12)
        .name("Birulabe")
        .basePrice(BigDecimal.valueOf(120.00))
        .finalPrice(BigDecimal.valueOf(120.00))
        .description("Meme club!")
        .weight(35.0d)
        .stock(10000)
        .type(Type.CLUB)
        .rarity(Rarity.COMMON)
        .blacksmithId(1L)
        .active(true)
        .build();
  }

  public static ItemPatchUpdateDto itemPatchUpdateDto() {
    return ItemPatchUpdateDto.builder()
        .material(Material.STEEL)
        .baseDamage(22)
        .baseDefense(10)
        .name("Patch Name")
        .basePrice(BigDecimal.valueOf(100.00))
        .finalPrice(BigDecimal.valueOf(99.99))
        .description("Patch Dagger")
        .weight(12.0f)
        .stock(5)
        .type(Type.DAGGER)
        .rarity(Rarity.RARE)
        .blacksmithId(2L)
        .active(false)
        .build();
  }

}
