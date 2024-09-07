package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.getPagedCategoryData;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.response.category.CategoriesPage;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.UserCategoryRepository;
import com.zero.pennywise.service.component.handler.CategoryHandler;
import com.zero.pennywise.service.component.handler.UserHandler;
import com.zero.pennywise.service.component.redis.CategoryCache;
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

  // 카테고리 수정
  @Transactional
  public String updateCategory(Long userId, UpdateCategoryDTO updateCategory) {

    UserEntity user = userHandler.getUserById(userId);
    String beforeCategoryName = updateCategory.getCategoryName();
    String newCategoryName = updateCategory.getNewCategoryName();

    CategoriesEntity category = categoryCache
        .getCategoryByCategoryName(user.getId(), beforeCategoryName);

    if (category.isShared()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "기본 카테고리는 수정할 수 없습니다.");
    }
    Long categoryId = category.getCategoryId();

    categoryCache.isCategoryNameExists(user.getId(), newCategoryName);

    CategoriesEntity newCategory = categoryHandler
        .updateOrCreateCategory(user, category, newCategoryName);

    categoryHandler.updateOther(user.getId(), categoryId, newCategory.getCategoryId());

    categoryCache.updateCategory(user.getId(), beforeCategoryName, newCategory);

    return "카테고리를 성공적으로 수정하였습니다.";
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