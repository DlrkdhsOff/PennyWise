package com.zero.pennywise.model.response.budget;

import com.zero.pennywise.entity.BudgetEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Budget {

  private Long budgetId;

  private String categoryName;

  private Long amount;

  public static Budget of(BudgetEntity budget) {
    return new Budget(
        budget.getBudgetId(),
        budget.getCategory().getCategoryName(),
        budget.getAmount());
  }
}
