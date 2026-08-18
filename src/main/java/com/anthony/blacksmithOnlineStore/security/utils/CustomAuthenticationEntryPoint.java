package com.anthony.blacksmithOnlineStore.security.utils;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.anthony.blacksmithOnlineStore.exceptions.handler.ExceptionDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final ObjectMapper objectMapper;

  public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException authException) throws IOException {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      ExceptionDetails exceptionDetails = new ExceptionDetails();
      exceptionDetails.setTitle("Unauthorized");
      exceptionDetails.setTimestamp(Instant.now());
      exceptionDetails.setStatus(HttpStatus.UNAUTHORIZED.value());
      exceptionDetails.setException(authException.getClass().toString());
      exceptionDetails.setPath(request.getRequestURI());
      exceptionDetails.setMessage(authException.getMessage());
      response.getWriter().write(objectMapper.writeValueAsString(exceptionDetails));
  }
}