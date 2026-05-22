package com.anthony.blacksmithOnlineStore.controller;

import com.anthony.blacksmithOnlineStore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;

  @PostMapping("/{id}/approve")
  public ResponseEntity<Void> approve(@PathVariable Long id) {
    paymentService.approved(id);
    return ResponseEntity.noContent().build();
  }
}
