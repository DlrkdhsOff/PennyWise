package com.zero.pennywise.model.response;

import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.model.type.SuccessResultCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ResultResponse {

  private final Integer code;
  private final HttpStatus status;
  private final String message;
  private final Object data;

  public ResultResponse(SuccessResultCode successResultCode, Object data) {
    this.code = successResultCode.getStatus().value();
    this.status = successResultCode.getStatus();
    this.message = successResultCode.getMessage();
    this.data = data;
  }

  public ResultResponse(FailedResultCode failedResultCode,  Object data) {
    this.code = failedResultCode.getStatus().value();
    this.status = failedResultCode.getStatus();
    this.message = failedResultCode.getMessage();
    this.data = data;
  }

  public static ResultResponse of(SuccessResultCode successResultCode) {
    return new ResultResponse(successResultCode, null);
  }

  public static ResultResponse of(FailedResultCode failedResultCode) {
    return new ResultResponse(failedResultCode, null);
  }

}
