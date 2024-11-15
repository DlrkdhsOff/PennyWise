package com.zero.pennywise.repository.querydsl.budget;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.budget.Budgets;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetQueryRepository {

  List<Budgets> getBudgetList(UserEntity user, String categoryName);

  Budgets getBudget(BudgetEntity budget);
}
