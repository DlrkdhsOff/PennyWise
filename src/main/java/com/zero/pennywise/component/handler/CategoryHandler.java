package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.repository.CategoriesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryHandler {

  private final CategoriesRepository categoriesRepository;


  public void existsCategory(Long userId, String categoryName) {
    if (categoriesRepository.existsByUserIdAndCategoryName(userId, categoryName)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다.");
    }
  }

  public CategoriesEntity getCateogry(Long userId, String categoryName) {
    return categoriesRepository.findByUserIdAndCategoryName(userId, categoryName)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));
  }

  public CategoriesEntity getCateogryByUserIdAndId(Long userId, Long categoryId) {
    return categoriesRepository.findByUserIdAndCategoryId(userId, categoryId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));
  }

  public List<CategoriesEntity> getAllCategoryList(Long userId) {
    return categoriesRepository.findAllByUserId(userId);
  }

  public CategoriesEntity getCategoryById(Long categoryId) {
    return categoriesRepository.findByCategoryId(categoryId);
  }
}
