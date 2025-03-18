package com.zero.pennywise.core.exception;

import com.zero.pennywise.domain.model.response.ResultResponse;
import com.zero.pennywise.domain.model.type.FailedResultCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

  private final ResultResponse resultResponse;

  public GlobalException(FailedResultCode failedResultCode) {
    super("");
    this.resultResponse = ResultResponse.of(failedResultCode);
  }

}
