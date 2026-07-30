package com.anthony.blacksmithOnlineStore.controller;

import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;
import com.anthony.blacksmithOnlineStore.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Payment", description = "Payment management")
public class PaymentController {
  private final PaymentService paymentService;

   @PostMapping("/order/{id}")
   @Operation(summary = "Simulate a payment of your own order")
    public ResponseEntity<PaymentResponseDto> create(@PathVariable Long id,
      @RequestBody PaymentCreateDto dto) {
        return ResponseEntity.ok(paymentService.createPayment(id, dto));
    }
}
