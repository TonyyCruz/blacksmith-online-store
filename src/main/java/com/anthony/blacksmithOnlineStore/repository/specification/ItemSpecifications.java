package com.anthony.blacksmithOnlineStore.repository.specification;

import com.anthony.blacksmithOnlineStore.controler.dto.item.ItemFilterDto;
import com.anthony.blacksmithOnlineStore.entity.Item;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecifications {

  public static Specification<Item> withFilters(ItemFilterDto filters) {
    return (root, query, criteriaBuilder) -> {
      var predicates = criteriaBuilder.conjunction();

      if (filters.name() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.like(root.get("name"), "%" + filters.name() + "%"));
      }
      if (filters.type() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.equal(root.get("type"), filters.type()));
      }
      if (filters.material() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.equal(root.get("material"), filters.material()));
      }
      if (filters.rarity() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.equal(root.get("rarity"), filters.rarity()));
      }
      if (filters.blacksmithId() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.equal(root.get("blacksmith").get("id"), filters.blacksmithId()));
      }
      if (filters.baseDamage() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.greaterThanOrEqualTo(root.get("baseDamage"), filters.baseDamage()));
      }
      if (filters.baseDefense() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.greaterThanOrEqualTo(root.get("baseDefense"), filters.baseDefense()));
      }
      if (filters.highestPrice() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.lessThanOrEqualTo(root.get("finalPrice"), filters.highestPrice()));
      }
      if (filters.lowestPrice() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.greaterThanOrEqualTo(root.get("finalPrice"), filters.lowestPrice()));
      }
      if (filters.maxWeight() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.lessThanOrEqualTo(root.get("weight"), filters.maxWeight()));
      }

      if (filters.active() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.equal(root.get("active"), filters.active()));
      }

      return predicates;
    };
  }
}
