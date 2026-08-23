package com.anthony.blacksmithOnlineStore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anthony.blacksmithOnlineStore.controller.docs.PaymentControllerDocs;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;
import com.anthony.blacksmithOnlineStore.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentControllerDocs {
  private final PaymentService paymentService;

  @Override
  @PostMapping("/order/{id}")
  public ResponseEntity<PaymentResponseDto> create(@PathVariable @Valid Long id,
    @RequestBody PaymentCreateDto dto) {
      return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(id, dto));
  }
}
