package com.anthony.blacksmithOnlineStore.controller;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;
import com.anthony.blacksmithOnlineStore.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
  private final PaymentService paymentService;

   @PostMapping("/order/{id}")
    public ResponseEntity<PaymentResponseDto> create(@PathVariable Long id,
      @RequestBody PaymentCreateDto dto) {
        return ResponseEntity.ok(paymentService.createPayment(id, dto));
    }
}
