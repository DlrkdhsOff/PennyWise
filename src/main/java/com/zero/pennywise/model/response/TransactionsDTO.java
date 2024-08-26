package com.zero.pennywise.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsDTO {
  private String type;
  private String categoryName;
  private Long amount;
  private String description;
  private String dateTime;

}
