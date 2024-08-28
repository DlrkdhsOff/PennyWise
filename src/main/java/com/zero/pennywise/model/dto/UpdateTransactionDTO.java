package com.zero.pennywise.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTransactionDTO {

  @NotNull(message = "거래 아이디를 입력해주세요.")
  private Long transactionId;

  private String categoryName;

  private String type;

  private Long amount;

  private String description;

  private String isFixed;
}
