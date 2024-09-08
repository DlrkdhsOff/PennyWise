package com.zero.pennywise.service.component.handler;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.service.component.cache.BudgetCache;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BudgetHandler {

  private final BudgetRepository budgetRepository;
  private final BudgetCache budgetCache;

  // 등록한 예산인지 유효값 검증
  public void validateBudget(Long userId, Long categoryId) {
    if (budgetRepository.existsByUserIdAndCategoryCategoryId(userId, categoryId)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 등록한 예산 입니다.");
    }
  }

  // 예산 새로 등록
  public BudgetEntity save(UserEntity user, CategoriesEntity category, Long amount) {
    return budgetRepository.save(
        BudgetEntity.builder()
            .user(user)
            .category(category)
            .amount(amount)
            .build());
  }

  // budget 수정및 캐시 남은 금액 update
  @Transactional
  public String updateBudget(UserEntity user, CategoriesEntity category, BudgetDTO budgetDTO) {
    return budgetRepository.findByUserIdAndCategoryCategoryId(user.getId(), category.getCategoryId())
        .map(budget -> {
          budget.setAmount(budgetDTO.getAmount());
          budgetRepository.save(budget);

          budgetCache.updateBudget(user.getId(), budget.getAmount(), budgetDTO.getCategoryName());
          return "성공적으로 예산을 수정하였습니다.";
        })
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "예산을 등록하지 않은 카테고리 입니다."));
  }

  // budget 삭제 및 캐시데이터 삭제
  @Transactional
  public String deleteBudget(Long userId, CategoriesEntity category) {
    return budgetRepository.findByUserIdAndCategoryCategoryId(userId, category.getCategoryId())
        .map(budget -> {
          budgetRepository.deleteByBudgetId(budget.getBudgetId());
          budgetCache.deleteBalance(userId, category.getCategoryName());

          return "성공적으로 예산을 삭제 하였습니다.";
        })
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "예산을 등록하지 않은 카테고리 입니다."));
  }

}
