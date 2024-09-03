package com.zero.pennywise.model.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BudgetsDTO {

  private String categoryName;
  private Long amount;
  private Long balance;

}
