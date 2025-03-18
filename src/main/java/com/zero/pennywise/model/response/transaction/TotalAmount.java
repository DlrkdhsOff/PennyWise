package com.zero.pennywise.model.response.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TotalAmount {

  private Long totalIncomeAmount;

  private Long totalExpensesAmount;
}
