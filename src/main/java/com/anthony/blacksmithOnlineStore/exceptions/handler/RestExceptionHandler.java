package com.anthony.blacksmithOnlineStore.exceptions.handler;

import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.BadRequestException;
import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.ForbiddenException;
import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.NotFoundException;
import com.anthony.blacksmithOnlineStore.exceptions.baseExceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ExceptionDetails> handleNotFoundException(NotFoundException e,
      HttpServletRequest request) {
    ExceptionDetails exceptionDetails = new ExceptionDetails();
    exceptionDetails.setTitle("Not found");
    exceptionDetails.setTimestamp(Instant.now());
    exceptionDetails.setStatus(HttpStatus.NOT_FOUND.value());
    exceptionDetails.setException(e.getClass().toString());
    exceptionDetails.setPath(request.getRequestURI());
    exceptionDetails.addError("error", e.getMessage());
    return ResponseEntity.status(exceptionDetails.getStatus()).body(exceptionDetails);
  }

  @ExceptionHandler({BadRequestException.class, DataAccessException.class})
  public ResponseEntity<ExceptionDetails> handleBadRequestException(Exception e,
      HttpServletRequest request) {
    ExceptionDetails exceptionDetails = new ExceptionDetails();
    exceptionDetails.setTitle("Bad request");
    exceptionDetails.setTimestamp(Instant.now());
    exceptionDetails.setStatus(HttpStatus.BAD_REQUEST.value());
    exceptionDetails.setException(e.getClass().toString());
    exceptionDetails.setPath(request.getRequestURI());
    exceptionDetails.addError("error", e.getMessage());
    return ResponseEntity.status(exceptionDetails.getStatus()).body(exceptionDetails);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ExceptionDetails> handleForbiddenException(ForbiddenException e,
      HttpServletRequest request) {
    ExceptionDetails exceptionDetails = new ExceptionDetails();
    exceptionDetails.setTitle("Forbidden");
    exceptionDetails.setTimestamp(Instant.now());
    exceptionDetails.setStatus(HttpStatus.FORBIDDEN.value());
    exceptionDetails.setException(e.getClass().toString());
    exceptionDetails.setPath(request.getRequestURI());
    exceptionDetails.addError("error", e.getMessage());
    return ResponseEntity.status(exceptionDetails.getStatus()).body(exceptionDetails);
  }

  @ExceptionHandler({AuthenticationException.class, UnauthorizedException.class})
  public ResponseEntity<ExceptionDetails> handleSpringAuthenticationException(
      Exception e,
      HttpServletRequest request) {
    ExceptionDetails exceptionDetails = new ExceptionDetails();
    exceptionDetails.setTitle("Unauthorized");
    exceptionDetails.setTimestamp(Instant.now());
    exceptionDetails.setStatus(HttpStatus.UNAUTHORIZED.value());
    exceptionDetails.setException(e.getClass().toString());
    exceptionDetails.setPath(request.getRequestURI());
    exceptionDetails.addError("error", e.getMessage());
    return ResponseEntity.status(exceptionDetails.getStatus()).body(exceptionDetails);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ExceptionDetails> handleSpringForbiddenException(AccessDeniedException e,
      HttpServletRequest request) {
    ExceptionDetails exceptionDetails = new ExceptionDetails();
    exceptionDetails.setTitle("Unauthorized");
    exceptionDetails.setTimestamp(Instant.now());
    exceptionDetails.setStatus(HttpStatus.FORBIDDEN.value());
    exceptionDetails.setException(e.getClass().toString());
    exceptionDetails.setPath(request.getRequestURI());
    exceptionDetails.addError("error", e.getMessage());
    return ResponseEntity.status(exceptionDetails.getStatus()).body(exceptionDetails);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ValidationDetails> invalidArgumentation(MethodArgumentNotValidException e,
      HttpServletRequest request) {
    ValidationDetails validationDetails = new ValidationDetails();
    validationDetails.setTitle("Bad request");
    validationDetails.setTimestamp(Instant.now());
    validationDetails.setStatus(HttpStatus.BAD_REQUEST.value());
    validationDetails.setException(e.getClass().toString());
    validationDetails.setPath(request.getRequestURI());
    validationDetails.addError("InvalidFieldData", e.getMessage());
    e.getBindingResult().getFieldErrors().forEach(objectError -> {
      validationDetails.addFieldError(objectError.getField(), objectError.getDefaultMessage());
    });
    return ResponseEntity.status(validationDetails.getStatus()).body(validationDetails);
  }

//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<ExceptionDetails> defaultException(Exception e,
//      HttpServletRequest request) {
//    ExceptionDetails exceptionDetails = new ExceptionDetails();
//    exceptionDetails.setTitle("Server Error");
//    exceptionDetails.setTimestamp(Instant.now());
//    exceptionDetails.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//    exceptionDetails.setException(e.getClass().toString());
//    exceptionDetails.setPath(request.getRequestURI());
//    exceptionDetails.addError("error", "Ops, algo deu errado. 😵");
//    return ResponseEntity.status(exceptionDetails.getStatus()).body(exceptionDetails);
//  }
}
