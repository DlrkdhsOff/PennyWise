package com.zero.pennywise.service;

import com.zero.pennywise.model.dto.CategoryDTO;
import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.budget.BudgetRepository;
import com.zero.pennywise.status.BudgetTrackerStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final BudgetRepository budgetRepository;
  private final CategoriesRepository categoriesRepository;

  // 카테고리 목록
  public List<String> getCategoryList(Long userId, String page) {

    return budgetRepository.getAllCategory(userId, page);
  }

  // 카테고리 생성
  public Response createCategory(Long userId, CategoryDTO categoryDTO) {
    return categoriesRepository.findByCategoryName(categoryDTO.getCategoryName())
        .map(category -> existingCategory(userId, category))
        .orElseGet(() -> createNewCategory(userId, categoryDTO));
  }

  // 카테고리 존재 여부 확인
  private Response existingCategory(Long userId, CategoriesEntity category) {
    // 사용자가 이미 등록한 카테고리일 경우
    if (budgetRepository.existsByUserIdAndCategoryId(userId, category.getCategoryId())) {
      return new Response(BudgetTrackerStatus.CATEGORY_ALREADY_EXISTS);
    }

    // category 테이블에는 존재하는 카테고리이지만 해당 사용자가 등록한 카테고리가 아닐경우
    createBudget(userId, category);
    return new Response(BudgetTrackerStatus.SUCCESS_CREATE_CATEGORY);
  }

  // category 테이블에 존재하지 않은 새로운 카테고리 일 경우
  private Response createNewCategory(Long userId, CategoryDTO categoryDTO) {
    CategoriesEntity category = categoriesRepository.save(
        CategoriesEntity.builder()
            .categoryName(categoryDTO.getCategoryName())
            .build()
    );

    createBudget(userId, category);
    return new Response(BudgetTrackerStatus.SUCCESS_CREATE_CATEGORY);
  }

  // 기본 예산 생성 메서드
  private void createBudget(Long userId, CategoriesEntity category) {
    budgetRepository.save(BudgetEntity.builder()
        .userId(userId)
        .categoryId(category.getCategoryId())
        .build());
  }

}
