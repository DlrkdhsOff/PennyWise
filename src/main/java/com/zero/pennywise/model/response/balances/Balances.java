package com.zero.pennywise.model.response.balances;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Balances {
  private String categoryName;

  private Long totalIncomeAmount;

  private Long totalExpensesAmount;

}
