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
    OrderItem orderItem = orderItemRepository.findById(id)
        .orElseThrow(() -> new OrderItemNotFoundException(id));
    if (authUser.isAdmin()) return orderItem;
    if (!authUser.getAuthenticatedId().equals(orderItem.getUserId())) {
      throw new ForbiddenOperationException(
          "You cannot cannot access this order item."
      );
    }
    return orderItem;
  }

}
