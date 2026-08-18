package com.anthony.blacksmithOnlineStore.security.utils;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.exceptions.handler.ExceptionDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  private final ObjectMapper objectMapper;

  public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(
    HttpServletRequest request,
    HttpServletResponse response,
    AccessDeniedException deniedException) throws IOException {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("application/json");
      ExceptionDetails exceptionDetails = new ExceptionDetails();
      exceptionDetails.setTitle("Forbidden");
      exceptionDetails.setTimestamp(Instant.now());
      exceptionDetails.setStatus(HttpStatus.FORBIDDEN.value());
      exceptionDetails.setException(deniedException.getClass().toString());
      exceptionDetails.setPath(request.getRequestURI());
      exceptionDetails.setMessage(deniedException.getMessage());
      response.getWriter().write(objectMapper.writeValueAsString(exceptionDetails));
  }
}