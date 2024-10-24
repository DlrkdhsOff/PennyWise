package com.zero.pennywise.service.impl;

import com.zero.pennywise.auth.jwt.JwtUtil;
import com.zero.pennywise.component.handler.CategoryHandler;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.type.SuccessResultCode;
import com.zero.pennywise.model.type.TokenType;
import com.zero.pennywise.repository.CategoryRepository;
import com.zero.pennywise.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final JwtUtil jwtUtil;
  private final UserHandler userHandler;
  private final CategoryHandler categoryHandler;

  // 카테고리 목록
  @Override
  public ResultResponse getCategoryList(int page, HttpServletRequest request) {
    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    UserEntity user = userHandler.findByUserId(userId);

    PageResponse<String> response = categoryHandler.getAllCategoryList(user, page);
    return new ResultResponse(SuccessResultCode.SUCCESS_GET_CATEGORY_LIST, response);
  }


  // 카테고리 생성
  @Override
  public ResultResponse createCategory(String categoryName, HttpServletRequest request) {

    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    UserEntity user = userHandler.findByUserId(userId);

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
    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    UserEntity user = userHandler.findByUserId(userId);

    CategoryEntity category = categoryHandler.findCategory(user, updateCategory.getBeforecategoryName());

    categoryHandler.validateCategory(user, updateCategory.getAfterCategoryName());

    CategoryEntity newCategory = CategoryEntity.builder()
        .categoryId(category.getCategoryId())
        .categoryName(updateCategory.getAfterCategoryName())
        .user(category.getUser())
        .build();

    categoryHandler.saveCategory(newCategory);

    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_CATEGORY);
  }


  // 카테고리 삭제
  @Override
  @Transactional
  public ResultResponse deleteCategory(String categoryName, HttpServletRequest request) {
    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    UserEntity user = userHandler.findByUserId(userId);

    CategoryEntity category = categoryHandler.findCategory(user, categoryName);

    categoryHandler.deleteCategory(category);

    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_CATEGORY);
  }
}