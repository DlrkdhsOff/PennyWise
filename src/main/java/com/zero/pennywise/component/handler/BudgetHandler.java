package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.response.balances.Balances;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.repository.BudgetRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetHandler {

  private final BudgetRepository budgetRepository;

  // 예산 검증
  public void validateBudget(UserEntity user, CategoryEntity category) {
    if (budgetRepository.existsByUserAndCategory(user, category)) {
      throw new GlobalException(FailedResultCode.BUDGET_ALREADY_USED);
    }
  }


  // 예산 새로 등록
  public BudgetEntity save(UserEntity user, CategoryEntity category, Long amount) {
    return budgetRepository.save(
        BudgetEntity.builder()
            .user(user)
            .category(category)
            .amount(amount)
            .build());
  }

  // 예산 조회
  public BudgetEntity findBudget(UserEntity user, CategoryEntity category) {
    return budgetRepository.findByUserAndCategory(user, category)
        .orElseThrow(() -> new GlobalException(FailedResultCode.BUDGET_NOT_FOUND));
  }

  // 예산 등록
  public void saveBudget(BudgetEntity budget) {
    budgetRepository.save(budget);
  }

  public void deleteBudget(BudgetEntity budget) {
  }
}
