package com.anthony.blacksmithOnlineStore.controller.docs.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.anthony.blacksmithOnlineStore.exceptions.handler.ValidationDetails;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
  responseCode = "400",
  description = "Request validation failed",
  content = @Content(
    schema = @Schema(implementation = ValidationDetails.class)
  ))
public @interface ApiBadValidationRequestDoc {
}
