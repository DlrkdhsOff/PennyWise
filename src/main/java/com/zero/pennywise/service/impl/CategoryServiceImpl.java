package com.zero.pennywise.service.impl;

import com.zero.pennywise.auth.jwt.JwtUtil;
import com.zero.pennywise.component.handler.CategoryHandler;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.type.SuccessResultCode;
import com.zero.pennywise.model.type.TokenType;
import com.zero.pennywise.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final JwtUtil jwtUtil;
  private final UserHandler userHandler;
  private final CategoryHandler categoryHandler;

  // 요청 헤더에서 사용자 정보를 추출하여 반환
  private UserEntity fetchUser(HttpServletRequest request) {
    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    return userHandler.findByUserId(userId);
  }

  // 사용자와 카테고리 이름으로 카테고리 엔티티 조회
  private CategoryEntity fetchCategory(UserEntity user, String categoryName) {
    return categoryHandler.findCategory(user, categoryName);
  }

  // 카테고리 목록
  @Override
  public ResultResponse getCategoryList(int page, HttpServletRequest request) {
    UserEntity user = fetchUser(request);
    PageResponse<String> response = categoryHandler.getAllCategoryList(user, page);

    return new ResultResponse(SuccessResultCode.SUCCESS_GET_CATEGORY_LIST, response);
  }


  // 카테고리 생성
  @Override
  public ResultResponse createCategory(String categoryName, HttpServletRequest request) {
    UserEntity user = fetchUser(request);
    categoryHandler.validateCategory(user, categoryName);
    CategoryEntity category = CategoryEntity.builder()
        .user(user)
        .categoryName(categoryName)
        .build();
    categoryHandler.saveCategory(category);

    return ResultResponse.of(SuccessResultCode.SUCCESS_CREATE_CATEGORY);
  }

  // 카테고리 수정
  @Override
  @Transactional
  public ResultResponse updateCategory(UpdateCategoryDTO updateCategory, HttpServletRequest request) {
    UserEntity user = fetchUser(request);
    CategoryEntity category = fetchCategory(user, updateCategory.getBeforecategoryName());

    categoryHandler.validateCategory(user, updateCategory.getAfterCategoryName());
    CategoryEntity newCategory = UpdateCategoryDTO.of(category, updateCategory);

    categoryHandler.saveCategory(newCategory);

    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_CATEGORY);
  }


  // 카테고리 삭제
  @Override
  @Transactional
  public ResultResponse deleteCategory(String categoryName, HttpServletRequest request) {
    UserEntity user = fetchUser(request);
    CategoryEntity category = fetchCategory(user, categoryName);
    categoryHandler.deleteCategory(category);

    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_CATEGORY);
  }
}