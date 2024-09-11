package com.zero.pennywise.model.response.transaction;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionPage {
  private int pageNumber;
  private int totalPage;
  private Long totalData;
  private List<TransactionsDTO> transactions;
}
