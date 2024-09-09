package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.response.category.CategoriesPage;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.querydsl.category.CategoryQueryRepository;
import com.zero.pennywise.component.handler.CategoryHandler;
import com.zero.pennywise.component.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoriesRepository categoriesRepository;
  private final CategoryQueryRepository categoryQueryRepository;
  private final UserHandler userHandler;
  private final CategoryHandler categoryHandler;

  // 카테고리 목록
  public CategoriesPage getCategoryList(Long userId, Pageable page) {
    UserEntity user = userHandler.getUserById(userId);

    Pageable pageable = page(page);

    Page<String> categories = categoryQueryRepository.getAllCategory(user.getId(), pageable);
    return CategoriesPage.of(categories);
  }


  // 카테고리 생성
  public String createCategory(Long userId, String categoryName) {
    UserEntity user = userHandler.getUserById(userId);

    categoryHandler.existsCategory(user.getId(), categoryName);

    categoriesRepository.save(CategoriesEntity.builder()
        .user(user)
        .categoryName(categoryName)
        .build());

    return "카테고리를 생성하였습니다.";
  }

  // 카테고리 수정
  @Transactional
  public String updateCategory(Long userId, UpdateCategoryDTO updateCategory) {
    UserEntity user = userHandler.getUserById(userId);

    return categoriesRepository.findByUserIdAndCategoryName(user.getId(), updateCategory.getCategoryName())
        .map(category -> {
          categoryHandler.existsCategory(user.getId(), updateCategory.getNewCategoryName());
          category.setCategoryName(updateCategory.getNewCategoryName());
          categoriesRepository.save(category);

          return "성공적으로 카테고리를 수정하였습니다.";
        })
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));
  }


  // 카테고리 삭제
  @Transactional
  public String deleteCategory(Long userId, String categoryName) {
    UserEntity user = userHandler.getUserById(userId);

    return categoriesRepository.findByUserIdAndCategoryName(user.getId(), categoryName)
        .map(category -> {
          categoriesRepository.deleteByUserIdAndCategoryName(user.getId(), categoryName);
          return "성공적으로 카테고리를 삭제하였습니다.";
        })
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));
  }
}