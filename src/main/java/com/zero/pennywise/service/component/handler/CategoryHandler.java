package com.zero.pennywise.service.component.handler;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserCategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.UserCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryHandler {

  private final UserCategoryRepository userCategoryRepository;
  private final CategoriesRepository categoriesRepository;

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


  // 다른 사용자가 카테고리를 사용하는지 확인
  public void isCategoryUsedByOtherUser(UserEntity user, Long categoryId, CategoriesEntity category) {
    if(userCategoryRepository.existsByUserNotAndCategoryCategoryId(user, categoryId)) {
      categoriesRepository.deleteByCategoryId(category.getCategoryId());
    }
  }

}
