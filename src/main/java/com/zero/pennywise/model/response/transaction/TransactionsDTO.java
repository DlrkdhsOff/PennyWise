package com.zero.pennywise.model.response.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsDTO {

  private Long transactionId;
  private String type;
  private String categoryName;
  private Long amount;
  private String description;
  private String dateTime;

  public static TransactionPage of(Page<TransactionsDTO> list) {
    for (TransactionsDTO transaction : list.getContent()) {
      switch (transaction.getType()) {
        case "EXPENSES" -> transaction.setType("지출");
        case "FIXED_EXPENSES" -> transaction.setType("고정 지출");
        case "INCOME" -> transaction.setType("수입");
        case "FIXED_INCOME" -> transaction.setType("고정 수입");
      };
    }

    return new TransactionPage(
        list.getNumber() + 1,
        list.getTotalPages(),
        list.getTotalElements(),
        list.getContent()
    );
  }

}
