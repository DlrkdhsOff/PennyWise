package com.zero.pennywise.service.component.cache;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.repository.querydsl.category.CategoryQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryCache {

  private final CacheManager cacheManager;
  private final CategoryQueryRepository categoryQueryRepository;

  // 캐시에 카테고리 목록 저장
  public void putCategoriesInCache(Long userId, List<CategoriesEntity> categories) {
    Cache cache = cacheManager.getCache("Category");
    if (cache != null) {
      cache.put(userId, categories);
    }
  }

  // 캐시에서 카테고리 목록 가져오기
  @SuppressWarnings("unchecked")
  public List<CategoriesEntity> getCategoriesFromCache(Long userId) {
    Cache cache = cacheManager.getCache("Category");
    List<CategoriesEntity> categories = null;

    if (cache != null) {
      categories = cache.get(userId, List.class);
    }

    if (categories == null) {
      categories = categoryQueryRepository.getAllCategory(userId);
    }

    return categories;
  }

  // 캐시에 새 카테고리 추가
  public void addNewCategory(Long userId, CategoriesEntity newCategory) {
    List<CategoriesEntity> categories = getCategoriesFromCache(userId);
    categories.add(newCategory);
    putCategoriesInCache(userId, categories);
  }

  // 캐시에서 카테고리 업데이트
  public void updateCategory(Long userId, String categoryName, CategoriesEntity newCategory) {
    List<CategoriesEntity> categories = getCategoriesFromCache(userId);

    for (CategoriesEntity category : categories) {
      if (category.getCategoryName().equals(categoryName)) {
        category.setCategoryId(newCategory.getCategoryId());
        category.setCategoryName(newCategory.getCategoryName());
        category.setShared(newCategory.isShared());
      }
    }

    putCategoriesInCache(userId, categories);
  }

  // 캐시에서 카테고리 삭제
  public void deleteCategory(Long userId, String categoryName) {
    List<CategoriesEntity> categories = getCategoriesFromCache(userId);
    categories.removeIf(category -> category.getCategoryName().equals(categoryName));
    putCategoriesInCache(userId, categories);
  }

  // 카테고리 중복 확인
  public void isCategoryNameExists(Long userId, String categoryName) {
    List<CategoriesEntity> categories = getCategoriesFromCache(userId);

    for (CategoriesEntity category : categories) {
      if (category.getCategoryName().equals(categoryName)) {
        throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다.");
      }
    }
  }

  // categoryName으로 CategoriesEntity 가져오기
  public CategoriesEntity getCategoryByCategoryName(Long userId, String categoryName) {
    List<CategoriesEntity> categories = getCategoriesFromCache(userId);

    for (CategoriesEntity category : categories) {
      if (category.getCategoryName().equals(categoryName)) {
        return category;
      }
    }
    throw new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다.");
  }

  // categoryId로 CategoriesEntity 가져오기
  public CategoriesEntity getCategoryByCategoryId(Long userId, Long categoryId) {
    List<CategoriesEntity> categories = getCategoriesFromCache(userId);

    for (CategoriesEntity category : categories) {
      if (category.getCategoryId().equals(categoryId)) {
        return category;
      }
    }
    throw new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다.");
  }
}
