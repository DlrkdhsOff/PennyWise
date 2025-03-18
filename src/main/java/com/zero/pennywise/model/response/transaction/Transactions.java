package com.zero.pennywise.model.response.transaction;

import com.zero.pennywise.entity.TransactionEntity;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transactions {

  private Long transactionId;

  private String type;

  private String categoryName;

  private Long amount;

  private String description;

  private String dateTime;

  private Long totalIncomeAmount;

  private Long totalExpensesAmount;

  public static List<Transactions> of(List<TransactionEntity> transaction) {
    return transaction.stream()
        .map(entity -> new Transactions(
            entity.getTransactionId(),
            entity.getType().getText(),
            entity.getCategory().getCategoryName(),
            entity.getAmount(),
            entity.getDescription(),
            entity.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            entity.getTotalIncomeAmount(),
            entity.getTotalExpensesAmount()
        ))
        .collect(Collectors.toList());
  }
}
