package com.zero.pennywise.model.request.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionInfoDTO {

  private String transactionType;
  private String categoryName;
  private Long month;
}
