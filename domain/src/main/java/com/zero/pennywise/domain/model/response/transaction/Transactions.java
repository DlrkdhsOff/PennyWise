package com.zero.pennywise.domain.model.response.transaction;

import com.zero.pennywise.domain.entity.TransactionEntity;
import com.zero.pennywise.domain.utils.FormatUtil;
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

  private String amount;

  private String description;

  private String dateTime;

  private String totalIncomeAmount;

  private String totalExpensesAmount;

  public static List<Transactions> of(List<TransactionEntity> transaction) {
    return transaction.stream()
        .map(entity -> new Transactions(
            entity.getTransactionId(),
            entity.getType().getText(),
            entity.getCategory().getCategoryName(),
            FormatUtil.formatWon(entity.getAmount()),
            entity.getDescription(),
            FormatUtil.formatTime(entity.getCreateAt()),
            FormatUtil.formatWon(entity.getTotalIncomeAmount()),
            FormatUtil.formatWon(entity.getTotalExpensesAmount())
        ))
        .collect(Collectors.toList());
  }
}
