package com.zero.pennywise.model.type;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FailedResultCode {

  ;
  private HttpStatus status;
  private String message;

}
