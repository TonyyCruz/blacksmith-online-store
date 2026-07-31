package com.anthony.blacksmithOnlineStore.service;

import com.anthony.blacksmithOnlineStore.entity.OrderItem;
import com.anthony.blacksmithOnlineStore.exceptions.ForbiddenOperationException;
import com.anthony.blacksmithOnlineStore.exceptions.OrderItemNotFoundException;
import com.anthony.blacksmithOnlineStore.repository.OrderItemRepository;
import com.anthony.blacksmithOnlineStore.security.utils.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {
  private final OrderItemRepository orderItemRepository;
  private final AuthenticatedUserService authUser;

  public OrderItem findEntityById(Long id) {
    if (!orderItemRepository.existsById(id)) throw new OrderItemNotFoundException(id);
    if (authUser.isAdmin()) return orderItemRepository.findById(id).get();
    return orderItemRepository.findByIdAndUserId(id, authUser.getAuthenticatedId())
        .orElseThrow(() -> new ForbiddenOperationException("You cannot access this order item."));
  }

}
