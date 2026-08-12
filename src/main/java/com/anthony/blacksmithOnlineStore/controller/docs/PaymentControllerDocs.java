package com.anthony.blacksmithOnlineStore.controller.docs;

import org.springframework.http.ResponseEntity;

import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBusinessViolationDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.SecuredApiResponses;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Payment", description = "Payment management")
public interface PaymentControllerDocs {

  @ApiResponse(
    responseCode = "201",
    description = "Payment created successfully",
    content = @Content(
    schema = @Schema(implementation = PaymentResponseDto.class)
  ))
  @ApiBusinessViolationDoc
  @SecuredApiResponses
  @Operation(summary = "Simulate a payment of your own order")
  public ResponseEntity<PaymentResponseDto> create(Long id, PaymentCreateDto dto);
}
