package com.anthony.blacksmithOnlineStore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.anthony.blacksmithOnlineStore.payment.PaymentProcessorFactory;
import com.anthony.blacksmithOnlineStore.payment.interface.PaymentProcessor;

@Service
@RequiredArgsConstructor
public class PaymentService {
  private final OrderService orderService;
  private final PaymentProcessorFactory factory;

  @Transactional
  public void approved(Long id) {
    orderService.orderPaid(id);
  }

  @Transactional
    public PaymentModel createPayment(
            CreatePaymentDTO dto) {

        PaymentProcessor processor =
                factory.getProcessor(dto.method());

        PaymentResult result =
                processor.process(dto);

        PaymentModel payment =
                new PaymentModel();

        payment.setAmount(dto.amount());
        payment.setMethod(dto.method());
        payment.setCreatedAt(LocalDateTime.now());

        payment.setTransactionId(
                result.transactionId()
        );

        payment.setStatus(
                result.success()
                        ? PaymentStatus.APPROVED
                        : PaymentStatus.REJECTED
        );

        return repository.save(payment);
    }
}
