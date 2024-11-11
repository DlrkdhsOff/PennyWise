package com.zero.pennywise.repository.querydsl.budget;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.budget.Budgets;
import com.zero.pennywise.model.response.page.PageResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetQueryRepository {

  PageResponse<Budgets> getBudgetInfo(UserEntity user, String categoryName, int page);
}
