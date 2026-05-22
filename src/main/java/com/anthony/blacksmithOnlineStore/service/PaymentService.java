package com.anthony.blacksmithOnlineStore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final OrderService orderService;

  @Transactional
  public void approved(Long id) {
    orderService.approve(id);
  }
}
