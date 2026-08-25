package com.anthony.blacksmithOnlineStore.repository;

import com.anthony.blacksmithOnlineStore.entity.Blacksmith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacksmithRepository extends JpaRepository<Blacksmith, Long> {

  Page<Blacksmith> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
