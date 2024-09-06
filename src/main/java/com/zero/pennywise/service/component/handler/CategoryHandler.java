package com.zero.pennywise.service.component.handler;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserCategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.UserCategoryRepository;
import com.zero.pennywise.repository.querydsl.budget.BudgetQueryRepository;
import com.zero.pennywise.repository.querydsl.category.CategoryQueryRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryHandler {

  private final UserCategoryRepository userCategoryRepository;
  private final CategoriesRepository categoriesRepository;
  private final CategoryQueryRepository categoryQueryRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final BudgetQueryRepository budgetQueryRepository;

  // category 테이블에 존재하지 않은 새로운 카테고리 일 경우
  public CategoriesEntity createNewCategory(String categoryName) {
    return categoriesRepository.save(
        CategoriesEntity.builder()
            .categoryName(categoryName)
            .build()
    );
  }

  // 사용자 카테고리 저장
  public void saveUserCategory(CategoriesEntity category, UserEntity user) {
    userCategoryRepository.save(
        UserCategoryEntity.builder()
            .category(category)
            .user(user)
            .build()
    );
  }


  // 새 카테고리 또는 기존 카테고리 업데이트
  public CategoriesEntity updateOrCreateCategory(UserEntity user, CategoriesEntity category,
      String newCategoryName) {

    return categoriesRepository.findByCategoryName(category.getCategoryName())
        .map(existingCategory -> {
          // 새로운 카테고리 이름이 있는지 확인
          return categoriesRepository.findByCategoryName(newCategoryName)
              .orElseGet(() -> createNewCategory(newCategoryName));
        })
        .orElseGet(() -> {
          // 새로운 카테고리 이름이 이미 존재하는지 확인
          return categoriesRepository.findByCategoryName(newCategoryName)
              .map(existingNewCategory -> {
                // 기존 카테고리를 삭제하고 새로운 카테고리를 반환
                categoriesRepository.deleteByCategoryId(category.getCategoryId());
                return existingNewCategory;
              })
              .orElseGet(() -> {
                // 새로운 카테고리가 존재하지 않으면, 기존 카테고리 이름을 변경하여 저장
                category.setCategoryName(newCategoryName);
                return categoriesRepository.save(category);
              });
        });
  }

  // 사용자 카테고리, 거래, 예산 카테고리 id 변경
  public void updateOther(Long userId, Long categoryId, Long newCategoryId) {
    categoryQueryRepository.updateCategory(userId, categoryId, newCategoryId);
    transactionQueryRepository.updateCategory(userId, categoryId, newCategoryId);
    budgetQueryRepository.updateCategory(userId, categoryId, newCategoryId);
  }


  // 다른 사용자가 카테고리를 사용하는지 확인
  public void isCategoryUsedByOtherUser(UserEntity user, Long categoryId, CategoriesEntity category) {
    if(userCategoryRepository.existsByUserNotAndCategoryCategoryId(user, categoryId)) {
      categoriesRepository.deleteByCategoryId(category.getCategoryId());
    }
  }

}
