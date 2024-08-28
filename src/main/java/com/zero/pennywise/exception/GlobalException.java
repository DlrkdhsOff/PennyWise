package com.zero.pennywise.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {
  HttpStatus status;

  public GlobalException(HttpStatus status, String message) {
    super();
  }
}
