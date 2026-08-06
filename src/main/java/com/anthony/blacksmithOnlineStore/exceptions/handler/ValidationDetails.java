package com.anthony.blacksmithOnlineStore.exceptions.handler;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(
  name = "Validate details",
  example = """
            {
              "title": "Bad request",
              "timestamp": "2026-08-05T15:10:12Z",
              "status": 400,
              "exception": "MethodArgumentNotValidException",
              "path": "/resource",
              "message": "Invalid field data",
              "fieldError": [
                {
                  "field": "fieldName",
                  "error": "Field must not be blank"
                },
                {
                  "field": "fieldName",
                  "error": "Field must be at least 2 characteres"
                }
              ]
            }
            """
)
@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationDetails extends ExceptionDetails {
  @Schema(description = "Invalid fields and their errors")
  private final List<FieldErrorMessage> fieldError = new ArrayList<>();

  public void addFieldError(String field, String error) {
    fieldError.add(new FieldErrorMessage(field, error));
  }
}
