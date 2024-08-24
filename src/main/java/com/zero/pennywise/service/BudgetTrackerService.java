package com.zero.pennywise.service;

import com.zero.pennywise.model.dto.BudgetDTO;
import com.zero.pennywise.model.dto.CategoryDTO;
import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.status.BudgetTrackerStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetTrackerService {

  private final CategoriesRepository categoriesRepository;
  private final BudgetRepository budgetRepository;

  // 카테고리 목록
  public List<CategoriesEntity> getCategoryList(Long userId) {
    List<BudgetEntity> budgetList = budgetRepository.findAllByUserId(userId);

    List<CategoriesEntity> categoryList = new ArrayList<>();

    for (BudgetEntity budget : budgetList) {
      Optional<CategoriesEntity> optional = categoriesRepository.findById(budget.getCategoryId());
      optional.ifPresent(categoryList::add);
    }

    return categoryList;
  }

  // 카테고리 생성
  public Response createCategory(Long userId, CategoryDTO categoryDTO) {
    if (categoriesRepository.existsByCategoryName(categoryDTO.getCategoryName())) {
      CategoriesEntity category = categoriesRepository.findByCategoryName(categoryDTO.getCategoryName());

      if (budgetRepository.existsByUserIdAndCategoryId(userId, category.getCategoryId())) {
        return new Response(BudgetTrackerStatus.CATEGORY_ALREADY_EXISTS);
      }
      createBudget(userId, category);
    } else {

      CategoriesEntity category = categoriesRepository.save(CategoriesEntity.builder()
          .categoryName(categoryDTO.getCategoryName())
          .shared(false)
          .build());

      createBudget(userId, category);
    }
    return new Response(BudgetTrackerStatus.SUCCESS_CREATE_CATEGORY);
  }

  // 카테고리별 예산 설정
  public Response setBudget(Long userId, BudgetDTO budgetDTO) {
    CategoriesEntity category = categoriesRepository.findByCategoryName(budgetDTO.getCategoryName());

    BudgetEntity budget = budgetRepository.findByUserIdAndCategoryId(userId,
        category.getCategoryId());

    budget.setAmount(budgetDTO.getAmount());
    budgetRepository.save(budget);

    return new Response(BudgetTrackerStatus.SUCCESS_SET_BUDGET);
  }


  // 기본 예산 생성 메서드
  public void createBudget(Long userId, CategoriesEntity category) {
    budgetRepository.save(BudgetEntity.builder()
        .userId(userId)
        .categoryId(category.getCategoryId())
        .build());
  }

}
