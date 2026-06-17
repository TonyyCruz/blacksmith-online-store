package com.anthony.blacksmithOnlineStore.controller;

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

  @PostMapping("/orders/{id}/approve")
  public ResponseEntity<Void> approve(@PathVariable Long id) {
    paymentService.approved(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
    public ResponseEntity<PaymentModel> create(
            @RequestBody CreatePaymentDTO dto) {

        PaymentModel payment =
                service.createPayment(dto);

        return ResponseEntity.ok(payment);
    }
}
