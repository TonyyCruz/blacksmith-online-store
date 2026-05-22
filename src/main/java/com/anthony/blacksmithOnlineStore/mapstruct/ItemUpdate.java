package com.anthony.blacksmithOnlineStore.mapstruct;

import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemPatchUpdateDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ItemUpdate {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "craftedBy", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "ratingAverage", ignore = true)
  void updateItemFromDto(ItemPatchUpdateDto dto, @MappingTarget Item item);
}
