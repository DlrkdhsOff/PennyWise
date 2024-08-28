package com.zero.pennywise.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionPage {
  private int pageNumber;
  private int totalPage;
  private Long totalData;
  private List<TransactionsDTO> transactions;
}
