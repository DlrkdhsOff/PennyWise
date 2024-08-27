package com.zero.pennywise.service;

import com.zero.pennywise.model.dto.BudgetDTO;
import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.budget.BudgetRepository;
import com.zero.pennywise.status.BudgetTrackerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {

  private final CategoriesRepository categoriesRepository;
  private final BudgetRepository budgetRepository;


  // 카테고리별 예산 설정
  public Response setBudget(Long userId, BudgetDTO budgetDTO) {
    return categoriesRepository.findByCategoryName(budgetDTO.getCategoryName())

        // 사용자가 등록한 카테고리 예산 설정
        .map(category -> {
          BudgetEntity budget = budgetRepository
              .findByUserIdAndCategoryId(userId, category.getCategoryId());

          budget.setAmount(budgetDTO.getAmount());
          budgetRepository.save(budget);

          return new Response(BudgetTrackerStatus.SUCCESS_SET_BUDGET);
        })

        // 사용자가 등록한 카테고리가 아닐 경우
        .orElse(new Response(BudgetTrackerStatus.CATEGORY_NOT_FOUND));
  }

}