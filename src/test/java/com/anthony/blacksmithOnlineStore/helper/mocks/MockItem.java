package com.anthony.blacksmithOnlineStore.helper.mocks;

import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemRequestDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import com.anthony.blacksmithOnlineStore.enums.Material;
import com.anthony.blacksmithOnlineStore.enums.Rarity;
import com.anthony.blacksmithOnlineStore.enums.Type;
import java.math.BigDecimal;

public class MockItem {

  public static Item item() {
    return Item.builder()
        .id(1L)
        .material(Material.ADAMANTIUM)
        .baseDamage(1000)
        .baseDefense(500)
        .name("Avalon blade")
        .basePrice(BigDecimal.valueOf(9999.99))
        .finalPrice(BigDecimal.valueOf(9999.99))
        .hasDiscount(false)
        .description("The sword of dreams")
        .weight(35.0f)
        .stock(1)
        .type(Type.LONG_SWORD)
        .rarity(Rarity.LEGENDARY)
        .craftedBy(MockBlacksmith.blacksmith())
        .build();
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
        .description("Meme club")
        .weight(35.0f)
        .stock(10000)
        .type(Type.CLUB)
        .rarity(Rarity.COMMON)
        .blacksmithId(1L)
        .active(true)
        .build();
  }

}
