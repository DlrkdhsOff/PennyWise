package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryHandler {

  private final CategoryRepository categoryRepository;
  private final RedisHandler redisHandler;

  // 모든 카테고리 조회
  public PageResponse<String> getAllCategoryList(UserEntity user, int page) {
    List<String> categories = categoryRepository.findAllByUser(user)
        .map(categoryList -> categoryList.stream()
            .map(CategoryEntity::getCategoryName).toList())
        .orElse(new ArrayList<>());

    return PageResponse.of(categories, page);
  }

  // 카테고리 검증
  public void validateCategory(UserEntity user, String categoryName) {
    if (categoryRepository.existsByUserAndCategoryName(user, categoryName)) {
      throw new GlobalException(FailedResultCode.CATEGORY_ALREADY_USED);
    }
  }

  // 카테고리 조회
  public CategoryEntity findCategory(UserEntity user, String categoryName) {
    return categoryRepository.findByUserAndCategoryName(user, categoryName)
        .orElseThrow(() -> new GlobalException(FailedResultCode.CATEGORY_NOT_FOUND));
  }


  // 카테고리 삭제
  public void deleteCategory(CategoryEntity category) {
    categoryRepository.delete(category);
  }


  public CategoryEntity getCateogryByUserIdAndId(Long userId, Long categoryId) {
    return categoryRepository.findByUserIdAndCategoryId(userId, categoryId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));
  }


  public void updateCategory(UserEntity user, CategoryEntity category, UpdateCategoryDTO updateCategory) {

    existsCategory(user.getId(), updateCategory.getNewCategoryName());

    redisHandler.updateCategoryName(user.getId(), category.getCategoryName(), updateCategory.getNewCategoryName());
    category.setCategoryName(updateCategory.getNewCategoryName());
    categoryRepository.save(category);
  }


  // 카테고리 저장
  public void saveCategory(CategoryEntity category) {
    categoryRepository.save(category);
  }

}
