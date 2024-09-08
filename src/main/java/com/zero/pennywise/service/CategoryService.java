package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.getPagedCategoryData;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.response.category.CategoriesPage;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.UserCategoryRepository;
import com.zero.pennywise.service.component.cache.CategoryCache;
import com.zero.pennywise.service.component.handler.CategoryHandler;
import com.zero.pennywise.service.component.handler.UserHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final UserCategoryRepository userCategoryRepository;
  private final CategoriesRepository categoriesRepository;
  private final UserHandler userHandler;
  private final CategoryCache categoryCache;
  private final CategoryHandler categoryHandler;

  // 카테고리 목록
  public CategoriesPage getCategoryList(Long userId, Pageable pageable) {
    List<CategoriesEntity> categories = categoryCache.getCategoriesFromCache(userId);
    categoryCache.putCategoriesInCache(userId, categories);
    return CategoriesPage.of(getPagedCategoryData(categories, pageable));
  }

  // 카테고리 생성
  public String createCategory(Long userId, String categoryName) {
    UserEntity user = userHandler.getUserById(userId);

    categoryCache.isCategoryNameExists(user.getId(), categoryName);

    CategoriesEntity newCategory = categoriesRepository.findByCategoryName(categoryName)
        .orElseGet(() -> categoryHandler.createNewCategory(categoryName));

    categoryCache.addNewCategory(userId, newCategory);
    categoryHandler.saveUserCategory(newCategory, user);
    return "카테고리를 생성하였습니다.";
  }


  // 카테고리 삭제
  @Transactional
  public String deleteCategory(Long userId, String categoryName) {
    UserEntity user = userHandler.getUserById(userId);

    CategoriesEntity category = categoryCache
        .getCategoryByCategoryName(user.getId(), categoryName);

    if (category.isShared()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "기본 카테고리는 삭제 할 수 없습니다.");
    }

    userCategoryRepository.deleteAllByUserIdAndCategoryCategoryId(user.getId(),
        category.getCategoryId());

    categoryHandler.isCategoryUsedByOtherUser(user, category.getCategoryId(), category);
    categoryCache.deleteCategory(user.getId(), categoryName);
    return "성공적으로 카테고리를 삭제하였습니다.";
  }

}