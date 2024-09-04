package com.zero.pennywise.redis;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.model.response.Categories;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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

  public void updateCategory(List<Categories> categories, Long userId, String categoryName, CategoriesEntity newCategory) {
    for (Categories category : categories) {
      if (category.getCategoryName().equals(categoryName)) {
        category.setCategoryId(newCategory.getCategoryId());
        category.setCategoryName(newCategory.getCategoryName());
      }
    }
    putCategoriesInCache(userId, categories);
  }

}
