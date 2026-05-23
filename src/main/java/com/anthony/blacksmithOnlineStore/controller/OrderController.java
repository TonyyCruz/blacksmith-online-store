package com.anthony.blacksmithOnlineStore.controller;

import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderPaymentDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderPaymentDto> create(
      @RequestBody OrderRequestDto dto, Authentication auth) {
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(dto));
  }
}
