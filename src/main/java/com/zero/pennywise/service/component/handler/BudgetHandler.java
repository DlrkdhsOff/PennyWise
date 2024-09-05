package com.zero.pennywise.service.component.handler;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetHandler {

  private final BudgetRepository budgetRepository;

  // 등록한 예산인지 유효값 검증
  public void validateBudget(Long userId, Long categoryId) {
    if (budgetRepository.existsByUserIdAndCategoryCategoryId(userId, categoryId)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 등록한 예산 입니다.");
    }
  }

  // 예산 조회
  public BudgetEntity getBudgetByUserIdAndCategoryId(Long userId, Long categoryId) {
    return budgetRepository.findByUserIdAndCategoryCategoryId(userId, categoryId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "예산이 등록되지 않은 카테고리입니다."));
  }

}
