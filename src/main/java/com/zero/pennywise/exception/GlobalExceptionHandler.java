package com.zero.pennywise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  // DTO 객체로 받는 매개변수 값에 null 값이 존재할 경우 처리하는 메서드
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
    StringBuilder errors = new StringBuilder();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      errors.append(error.getDefaultMessage()).append("\n");
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
  }


  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<String> globalException(GlobalException ex) {
    return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
  }
}
