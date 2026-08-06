package com.anthony.blacksmithOnlineStore.exceptions.handler;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDetails implements Serializable {
  private String title;
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
      timezone = "GMT")
  private Instant timestamp;
  private int status;
  private String exception;
  private String path;
  private String message;
}
