package com.anthony.blacksmithOnlineStore.mapstruct;

import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ItemUpdate {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateItemFromDto(ItemPatchUpdateDto dto, @MappingTarget Item item);
}
