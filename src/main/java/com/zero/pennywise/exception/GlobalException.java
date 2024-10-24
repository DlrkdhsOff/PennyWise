package com.zero.pennywise.exception;

import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.model.type.FailedResultCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

  private final ResultResponse resultResponse;

  public GlobalException(FailedResultCode failedResultCode) {
    super("");
    this.resultResponse = ResultResponse.of(failedResultCode);
  }

}
