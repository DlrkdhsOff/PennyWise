package com.zero.pennywise.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
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
      if ("FIXED_EXPENSES".equals(transaction.getType())) {
        transaction.setType("고정 지출");
      } else if ("FIXED_INCOME".equals(transaction.getType())) {
        transaction.setType("고정 수입");
      } else if ("EXPENSES".equals(transaction.getType())) {
        transaction.setType("지출");
      } else {
        transaction.setType("수입");
      }
    }

    return new TransactionPage(
        list.getNumber() + 1,
        list.getTotalPages(),
        list.getTotalElements(),
        list.getContent()
    );
  }

}
