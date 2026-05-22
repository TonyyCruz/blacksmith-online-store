package com.anthony.blacksmithOnlineStore.repository.specification;

import com.anthony.blacksmithOnlineStore.controller.dto.item.ItemFilterDto;
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
      if (filters.material() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.equal(root.get("material"), filters.material()));
      }
      if (filters.minDamage() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.greaterThanOrEqualTo(root.get("baseDamage"), filters.minDamage()));
      }
      if (filters.maxDamage() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.lessThanOrEqualTo(root.get("baseDamage"), filters.maxDamage()));
      }
      if (filters.minDefense() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.greaterThanOrEqualTo(root.get("baseDefense"), filters.minDefense()));
      }
      if (filters.maxDefense() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.lessThanOrEqualTo(root.get("baseDefense"), filters.maxDefense()));
      }
      if (filters.minPrice() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.greaterThanOrEqualTo(root.get("finalPrice"), filters.minPrice()));
      }
      if (filters.maxPrice() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.lessThanOrEqualTo(root.get("finalPrice"), filters.maxPrice()));
      }
      if (filters.minWeight() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.greaterThanOrEqualTo(root.get("weight"), filters.minWeight()));
      }
      if (filters.maxWeight() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.lessThanOrEqualTo(root.get("weight"), filters.maxWeight()));
      }
      if (filters.type() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.equal(root.get("type"), filters.type()));
      }
      if (filters.rarity() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.equal(root.get("rarity"), filters.rarity()));
      }
      if (filters.blacksmithId() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.equal(root.get("craftedBy").get("id"), filters.blacksmithId()));
      }
      if (filters.active() != null) {
        predicates = criteriaBuilder.and(predicates,
            criteriaBuilder.equal(root.get("active"), filters.active()));
      }
      return predicates;
    };
  }
}
