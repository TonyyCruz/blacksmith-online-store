package com.anthony.blacksmithOnlineStore.controller.docs.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.anthony.blacksmithOnlineStore.exceptions.handler.ExceptionDetails;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
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
                "exception": "ForbiddenOperationException",
                "path": "/resource",
                "message": "You must be authenticated to access this resource."
              """
)))
@ApiUnauthorizedDoc
public @interface ApiForbiddenDoc {
}
