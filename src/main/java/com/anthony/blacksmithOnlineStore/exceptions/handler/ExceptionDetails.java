package com.anthony.blacksmithOnlineStore.exceptions.handler;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDetails implements Serializable {
   @Schema(description = "Exception title")
  private String title;
  @Schema(description = "Exception ocurrence date")
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
      timezone = "GMT")
  private Instant timestamp;
  @Schema(description = "Exception status code")
  private int status;
  @Schema(description = "Exception class name")
  private String exception;
  @Schema(description = "Exception ocurrence path")
  private String path;
  @Schema(description = "Exception error message")
  private String message;
}
