package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.response.balances.Balances;
import com.zero.pennywise.model.response.budget.Budgets;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.response.transaction.Transactions;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.querydsl.budget.BudgetQueryRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetHandler {

  private final BudgetRepository budgetRepository;
  private final BudgetQueryRepository budgetQueryRepository;

  // 예산 목록
  public PageResponse<Budgets> getBudgetInfo(UserEntity user, String categoryName, int page) {
    return budgetQueryRepository.getBudgetInfo(user, categoryName, page);
  }

  // 예산 중복 검증
  public void validateBudget(UserEntity user, CategoryEntity category) {
    if (budgetRepository.existsByUserAndCategory(user, category)) {
      throw new GlobalException(FailedResultCode.BUDGET_ALREADY_USED);
    }
  }

  // 예산 등록
  public void saveBudget(BudgetEntity budget) {
    budgetRepository.save(budget);
  }

  // 예산 조회
  public BudgetEntity findBudgetById(Long budgetId, UserEntity user) {
    return budgetRepository.findByBudgetIdAndUser(budgetId, user)
        .orElseThrow(() -> new GlobalException(FailedResultCode.BUDGET_NOT_FOUND));
  }

  // 예산 삭제
  public void deleteBudget(BudgetEntity budget) {
    budgetRepository.delete(budget);
  }
}
