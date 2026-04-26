package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.exceptions.OrderItemNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {
  private final OrderItemRepository orderItemRepository;

  public OrderItem findEntityById(Long id) {
    return orderItemRepository.findById(id).orElseThrow(() -> new OrderItemNotFoundException(id));
  }

}
