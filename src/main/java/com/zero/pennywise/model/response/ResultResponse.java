package com.zero.pennywise.model.response;

import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.model.type.SuccessResultCode;
import org.springframework.http.HttpStatus;


public class ResultResponse {

  private HttpStatus status;
  private String message;
  private Object data;

  private ResultResponse(SuccessResultCode successResultCode, Object data) {
    this.status = successResultCode.getStatus();
    this.message = successResultCode.getMessage();
    this.data = data;
  }

  private ResultResponse(FailedResultCode failedResultCode,  Object data) {
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
