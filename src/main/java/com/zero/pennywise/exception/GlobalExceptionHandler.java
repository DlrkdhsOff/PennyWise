package com.zero.pennywise.exception;

import com.zero.pennywise.model.response.ResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  // DTO 객체로 받는 매개변수 값에 null 값이 존재할 경우 처리하는 메서드
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResultResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    StringBuilder errors = new StringBuilder();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      errors.append(error.getDefaultMessage()).append("\n");
    });

    ResultResponse response = new ResultResponse(HttpStatus.BAD_REQUEST, errors.toString());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<ResultResponse> handleGlobalException(GlobalException ex) {
    ResultResponse resultResponse = ex.getResultResponse();
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }
}
