package com.anthony.blacksmithOnlineStore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderResponseDto;
import com.anthony.blacksmithOnlineStore.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<OrderResponseDto> create(
      @Valid @RequestBody OrderRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(dto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.findById(id));
  }

  @GetMapping
  public ResponseEntity<List<OrderResponseDto>> getOrders() {
    return ResponseEntity.ok(orderService.getUserOrders());
  }

  @PostMapping("/request/{id}/return")
  public ResponseEntity<Void> returnRequest(@PathVariable Long id) {
    orderService.returnRequest(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/request/{id}/refund")
  public ResponseEntity<Void> refundRequest(@PathVariable Long id) {
      orderService.refundRequest(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/request/{id}/cancel")
  public ResponseEntity<Void> cancel(@PathVariable Long id) {
      orderService.cancel(id);
      return ResponseEntity.noContent().build();
  }
  
}
