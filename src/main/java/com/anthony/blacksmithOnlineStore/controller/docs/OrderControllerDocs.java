package com.anthony.blacksmithOnlineStore.controller.docs;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.anthony.blacksmithOnlineStore.controller.docs.anotations.ApiBusinessViolationDoc;
import com.anthony.blacksmithOnlineStore.controller.docs.anotations.SecuredApiResponses;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderRequestDto;
import com.anthony.blacksmithOnlineStore.controller.dto.order.OrderResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Orders", description = "Order management")
public interface OrderControllerDocs {

  @ApiResponse(
    responseCode = "201",
    description = "Order created successfully",
    content = @Content(
    schema = @Schema(implementation = OrderResponseDto.class)
  ))
  @ApiBusinessViolationDoc
  @SecuredApiResponses
  @Operation(summary = "Create a new order")
  public ResponseEntity<OrderResponseDto> create(OrderRequestDto dto);

  @ApiResponse(
    responseCode = "200",
    description = "Order found successfully",
    content = @Content(
    schema = @Schema(implementation = OrderResponseDto.class)
  ))
  @SecuredApiResponses
  @Operation(summary = "Find your own order by id")
  public ResponseEntity<OrderResponseDto> getOrderById(Long id);

  @ApiResponse(
    responseCode = "200",
    description = "Page of orders"
  )
  @SecuredApiResponses
  @Operation(summary = "Find all your own orders")
  public ResponseEntity<List<OrderResponseDto>> getOrders();

  @ApiResponse(
    responseCode = "204",
    description = "Return requested"
  )
  @ApiBusinessViolationDoc
  @SecuredApiResponses
  @Operation(summary = "Request a return of your own order by id")
  public ResponseEntity<Void> returnRequest(Long id);

  @ApiResponse(
    responseCode = "204",
    description = "Refound requested"
  )
  @ApiBusinessViolationDoc
  @SecuredApiResponses
  @Operation(summary = "Request a refound of your own payd order by id")
  public ResponseEntity<Void> refundRequest(Long id);

  @ApiResponse(
    responseCode = "204",
    description = "Order canceled"
  )
  @ApiBusinessViolationDoc
  @SecuredApiResponses
  @Operation(summary = "Cancel your own order by id")
  public ResponseEntity<Void> cancel(Long id);
}
