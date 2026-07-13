package com.anthony.blacksmithOnlineStore.repository;

import com.anthony.blacksmithOnlineStore.entity.Order;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByUserId(UUID userId);
}
