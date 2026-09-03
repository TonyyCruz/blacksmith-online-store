package com.anthony.blacksmithOnlineStore.controller.docs;

import org.springframework.http.ResponseEntity;

import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBadValidationRequestDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBusinessViolationDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiConflictDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiForbiddenDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiNotFoundDoc;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentCreateDto;
import com.anthony.blacksmithOnlineStore.controller.dto.payment.PaymentResponseDto;
import com.anthony.blacksmithOnlineStore.exceptions.handler.ExceptionDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Payments")
public interface PaymentControllerDocs {

  @ApiResponse(
    responseCode = "201",
    description = "Payment created successfully",
    content = @Content(
    schema = @Schema(implementation = PaymentResponseDto.class)))
  @ApiResponse(
    responseCode = "402",
    description = "Payment required",
    content = @Content(
      schema = @Schema(implementation = ExceptionDetails.class),
      examples = @ExampleObject(
        name = "Exception details",
        value = """
              {
                "title": "Payment Required",
                "timestamp": "2026-09-05T15:10:12Z",
                "status": 402,
                "exception": "PaymentRefusedException",
                "path": "/resource",
                "message": "The payment was declined due to insufficient funds"
              """)))
  @ApiConflictDoc
  @ApiNotFoundDoc
  @ApiForbiddenDoc
  @ApiBusinessViolationDoc
  @ApiBadValidationRequestDoc
  @Operation(summary = "Simulate a payment of your own order")
  public ResponseEntity<PaymentResponseDto> create(Long id, PaymentCreateDto dto);
}
