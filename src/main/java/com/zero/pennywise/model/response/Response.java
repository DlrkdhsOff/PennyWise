package com.zero.pennywise.model.response;

import com.zero.pennywise.status.AccountStatus;
import com.zero.pennywise.status.BudgetTrackerStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Response {
  private final HttpStatus status;

  private final String message;

  public Response(AccountStatus status){
    this.status = status.getStatus();
    this.message = status.getMessage();
  }

  public Response(BudgetTrackerStatus status){
    this.status = status.getStatus();
    this.message = status.getMessage();
  }
}
