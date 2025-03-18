package com.zero.pennywise.model.response.budget;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BudgetCache {

  private List<Budgets> budgets = new ArrayList<>();

  public BudgetCache(List<Budgets> budgets) {
    this.budgets = budgets;
  }
}
// rrr