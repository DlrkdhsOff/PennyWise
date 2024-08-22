package com.zero.pennywise.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
    StringBuilder errors = new StringBuilder();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      errors.append(error.getDefaultMessage()).append("\n");
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
  }
}
