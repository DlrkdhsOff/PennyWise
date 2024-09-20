package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryHandler {

  private final CategoryRepository categoryRepository;
  private final RedisHandler redisHandler;


  public void existsCategory(Long userId, String categoryName) {
    if (categoryRepository.existsByUserIdAndCategoryName(userId, categoryName)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다.");
    }
  }

  public CategoryEntity getCateogry(Long userId, String categoryName) {
    return categoryRepository.findByUserIdAndCategoryName(userId, categoryName)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));
  }

  public CategoryEntity getCateogryByUserIdAndId(Long userId, Long categoryId) {
    return categoryRepository.findByUserIdAndCategoryId(userId, categoryId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));
  }

  public List<CategoryEntity> getAllCategoryList(Long userId) {
    return categoryRepository.findAllByUserId(userId);
  }

  public void updateCategory(UserEntity user, CategoryEntity category, UpdateCategoryDTO updateCategory) {

    existsCategory(user.getId(), updateCategory.getNewCategoryName());

    redisHandler.updateCategoryName(user.getId(), category.getCategoryName(), updateCategory.getNewCategoryName());
    category.setCategoryName(updateCategory.getNewCategoryName());
    categoryRepository.save(category);
  }
}
