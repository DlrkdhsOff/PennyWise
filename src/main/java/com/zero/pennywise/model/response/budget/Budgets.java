package com.zero.pennywise.model.response.budget;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Budgets {

  private Long budgetId;

  private String categoryName;

  private Long amount;

  private Long balance;

}
