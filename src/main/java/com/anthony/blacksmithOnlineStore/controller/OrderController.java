package com.anthony.blacksmithOnlineStore.controller;

import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderPaymentDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderResponseDto;
import com.anthony.blacksmithOnlineStore.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Orders", description = "Order management")
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  @Operation(summary = "Create a order")
  public ResponseEntity<OrderPaymentDto> create(
      @Valid @RequestBody OrderRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(dto));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find your own order by id")
  public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.getById(id));
  }

  @GetMapping
  @Operation(summary = "Find all your own orders")
  public ResponseEntity<List<OrderResponseDto>> getOrders() {
    return ResponseEntity.ok(orderService.getUserOrders());
  }

  @PostMapping("/request/{id}/return")
  @Operation(summary = "Request a return of your own order by id")
  public ResponseEntity<OrderResponseDto> returnRequest(@PathVariable Long id) {
      return ResponseEntity.ok(orderService.returnRequest(id));
  }

  @PostMapping("/request/{id}/refund")
  @Operation(summary = "Request a refound of your own payd order by id")
  public ResponseEntity<OrderResponseDto> refundRequest(@PathVariable Long id) {
      return ResponseEntity.ok(orderService.refundRequest(id));
  }

  @PostMapping("/request/{id}/cancel")
  @Operation(summary = "Cancel your own order by id")
  public ResponseEntity<Void> cancel(@PathVariable Long id) {
      orderService.cancel(id);
      return ResponseEntity.noContent().build();
  }
  
}
