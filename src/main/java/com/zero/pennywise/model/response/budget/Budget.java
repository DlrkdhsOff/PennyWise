package com.zero.pennywise.model.response.budget;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.utils.FormatUtil;
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

  private String amount;

  public static Budget of(BudgetEntity budget) {
    return new Budget(
        budget.getBudgetId(),
        budget.getCategory().getCategoryName(),
        FormatUtil.formatWon(budget.getAmount()));
  }
}
