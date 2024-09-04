package com.zero.pennywise.redis;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.response.Categories;
import java.util.Iterator;
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

  // 캐시에서 카테고리 목록 가져오기
  @SuppressWarnings("unchecked")
  public List<Categories> getCategoriesFromCache(Long userId) {
    Cache cache = cacheManager.getCache("Category");
    return cache != null ? cache.get(userId, List.class) : null;
  }

  // 캐시에 카테고리 목록 저장
  public void putCategoriesInCache(Long userId, List<Categories> categories) {
    Cache cache = cacheManager.getCache("Category");
    if (cache != null) {
      cache.put(userId, categories);
    }
  }

  // 카테고리 생성시 캐시에 생성한 카테고리 저장
  public void putNewCategoryInCache(List<Categories> categories, Long userId, CategoriesEntity category) {
    if (categories != null) {
      categories.add(new Categories(category.getCategoryId(), category.getCategoryName()));
      putCategoriesInCache(userId, categories);
    }
  }

  // 캐시 데이터 업데이트
  public void updateCategory(List<Categories> categories, Long userId, String categoryName, CategoriesEntity newCategory) {
    for (Categories category : categories) {
      if (category.getCategoryName().equals(categoryName)) {
        category.setCategoryId(newCategory.getCategoryId());
        category.setCategoryName(newCategory.getCategoryName());
      }
    }
    putCategoriesInCache(userId, categories);
  }

  // 캐시 데이터 삭제
  public void deleteCategory(List<Categories> categories, Long userId, String categoryName) {
    categories.removeIf(category -> category.getCategoryName().equals(categoryName));

    // 삭제 후 캐시에 업데이트
    putCategoriesInCache(userId, categories);
  }

  // 해당 카테고리가 존재하는지 확인
  public boolean isCategoryNameExists(List<Categories> categories, String categoryName) {
    for (Categories category : categories) {
      if (category.getCategoryName().equals(categoryName)) {
        return true;
      }
    }
    return false;
  }

  // Categories 객체 반환
  public Categories getCategory(List<Categories> categories, String categoryName) {
    for (Categories category : categories) {
      if (category.getCategoryName().equals(categoryName)) {
        return category;
      }
    }
    throw new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다.");
  }

}
