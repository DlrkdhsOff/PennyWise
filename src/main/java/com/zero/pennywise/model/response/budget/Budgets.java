package com.zero.pennywise.model.response.budget;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Budgets {

  private Long budgetId;

  private String categoryName;

  private Long amount;

  private Long balance;

}
