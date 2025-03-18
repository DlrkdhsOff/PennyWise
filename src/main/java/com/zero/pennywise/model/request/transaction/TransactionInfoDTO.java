package com.zero.pennywise.model.request.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInfoDTO {

  private String transactionType;

  private String categoryName;

  private Long month;

  private int page;
}
