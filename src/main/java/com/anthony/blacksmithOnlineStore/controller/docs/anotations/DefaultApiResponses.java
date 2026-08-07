package com.anthony.blacksmithOnlineStore.controller.docs.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.anthony.blacksmithOnlineStore.exceptions.handler.ExceptionDetails;
import com.anthony.blacksmithOnlineStore.exceptions.handler.ValidationDetails;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
  @ApiResponse(responseCode = "400",
  description = "Request validation failed",
  content = @Content(
    schema = @Schema(implementation = ValidationDetails.class)
  )),
  @ApiResponse(
    responseCode = "401",
    description = "Authentication required",
    content = @Content(
      schema = @Schema(implementation = ExceptionDetails.class),
      examples = @ExampleObject(
        name = "Exception details",
        value = """
              {
                "title": "Unauthorized",
                "timestamp": "2026-08-05T15:10:12Z",
                "status": 401,
                "exception": "UnauthorizedException",
                "path": "/resource",
                "message": "You must be authenticated to acess this resource"
              """
      )
    )),
  @ApiResponse(
    responseCode = "403",
    description = "Access denied",
    content = @Content(
      schema = @Schema(implementation = ExceptionDetails.class),
      examples = @ExampleObject(
        name = "Exception details",
        value = """
              {
                "title": "Forbidden",
                "timestamp": "2026-08-05T15:10:12Z",
                "status": 403,
                "exception": "ForbiddenException",
                "path": "/resource",
                "message": "You must be authenticated to access this resource."
              """
      )
    )),
  @ApiResponse(
    responseCode = "404",
    description = "Resource not found",
    content = @Content(
      schema = @Schema(implementation = ExceptionDetails.class),
      examples = @ExampleObject(
        name = "Exception details",
        value = """
              {
                "title": "Not Found",
                "timestamp": "2026-08-05T15:10:12Z",
                "status": 404,
                "exception": "ResourceNotFoundException",
                "path": "/resource",
                "message": "The requested resource was not found."
              """
      )
    )),
    @ApiResponse(
    responseCode = "409",
    description = "Conflict with current resource state",
    content = @Content(
      schema = @Schema(implementation = ExceptionDetails.class),
      examples = @ExampleObject(
        name = "Exception details",
        value = """
              {
                "title": "Conflict",
                "timestamp": "2026-08-05T15:10:12Z",
                "status": 409,
                "exception": "ConflictStateDataException",
                "path": "/resource",
                "message": "Conflict with current resource state"
              """
      )
    )),
    @ApiResponse(
    responseCode = "422",
    description = "Business rule violation",
    content = @Content(
      schema = @Schema(implementation = ExceptionDetails.class),
      examples = @ExampleObject(
        name = "Exception details",
        value = """
              {
                "title": "Unprocessable Entity",
                "timestamp": "2026-08-05T15:10:12Z",
                "status": 422,
                "exception": "BusinessException",
                "path": "/resource",
                "message": "This operation violates a business rule."
              """
      )
    ))
})
public @interface DefaultApiResponses {
}
