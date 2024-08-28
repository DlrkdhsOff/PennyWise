package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.CategoryDTO;
import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.model.response.CategoriesPage;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.querydsl.CategoryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final BudgetRepository budgetRepository;
  private final CategoriesRepository categoriesRepository;
  private final CategoryQueryRepository categoryQueryRepository;

  // 카테고리 목록
  public CategoriesPage getCategoryList(Long userId, Pageable page) {
    Pageable pageable = page(page);

    return CategoriesPage
        .of(categoryQueryRepository.getAllCategory(userId, pageable));
  }

  // 카테고리 생성
  public String createCategory(Long userId, CategoryDTO categoryDTO) {
    return categoriesRepository.findByCategoryName(categoryDTO.getCategoryName())
        .map(category -> existingCategory(userId, category))
        .orElseGet(() -> createNewCategory(userId, categoryDTO));
  }

  // 카테고리 존재 여부 확인
  private String existingCategory(Long userId, CategoriesEntity category) {
    // 사용자가 이미 등록한 카테고리일 경우
    if (budgetRepository.existsByUserIdAndCategoryId(userId, category.getCategoryId())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다.");
    }

    // category 테이블에는 존재하는 카테고리이지만 해당 사용자가 등록한 카테고리가 아닐경우
    createBudget(userId, category);
    return "카테고리를 생성하였습니다.";
  }

  // category 테이블에 존재하지 않은 새로운 카테고리 일 경우
  private String createNewCategory(Long userId, CategoryDTO categoryDTO) {
    CategoriesEntity category = categoriesRepository.save(
        CategoriesEntity.builder()
            .categoryName(categoryDTO.getCategoryName())
            .build()
    );

    createBudget(userId, category);
    return "카테고리를 생성하였습니다.";
  }

  // 기본 예산 생성 메서드
  private void createBudget(Long userId, CategoriesEntity category) {
    budgetRepository.save(BudgetEntity.builder()
        .userId(userId)
        .categoryId(category.getCategoryId())
        .build());
  }

}
