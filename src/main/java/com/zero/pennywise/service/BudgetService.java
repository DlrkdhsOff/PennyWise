package com.zero.pennywise.service;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.BudgetDTO;
import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {

  private final CategoriesRepository categoriesRepository;
  private final BudgetRepository budgetRepository;


  // 카테고리별 예산 설정
  public String setBudget(Long userId, BudgetDTO budgetDTO) {
    return categoriesRepository.findByCategoryName(budgetDTO.getCategoryName())

        // 사용자가 등록한 카테고리 예산 설정
        .map(category -> {
          BudgetEntity budget = budgetRepository
              .findByUserIdAndCategoryCategoryId(userId, category.getCategoryId());

          budget.setAmount(budgetDTO.getAmount());
          budgetRepository.save(budget);

          return "성공적으로 예산을 설정 했습니다.";
        })

        // 사용자가 등록한 카테고리가 아닐 경우
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다."));
  }

}