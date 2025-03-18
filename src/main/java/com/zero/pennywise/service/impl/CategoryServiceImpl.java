//package com.zero.pennywise.service.impl;
//
//import com.zero.pennywise.component.facade.UserFacade;
//import com.zero.pennywise.entity.CategoryEntity;
//import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
//import com.zero.pennywise.model.response.ResultResponse;
//import com.zero.pennywise.model.response.page.PageResponse;
//import com.zero.pennywise.model.type.SuccessResultCode;
//import com.zero.pennywise.service.CategoryService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class CategoryServiceImpl implements CategoryService {
//
//  private final UserFacade userFacade;
//
//  // 카테고리 목록
//  @Override
//  public ResultResponse getCategoryList(int page, HttpServletRequest request) {
//    PageResponse<String> response = userFacade.getUserCategoryList(request, page);
//
//    return new ResultResponse(SuccessResultCode.SUCCESS_GET_CATEGORY_LIST, response);
//  }
//
//
//  // 카테고리 생성
//  @Override
//  public ResultResponse createCategory(String categoryName, HttpServletRequest request) {
//    CategoryEntity category = userFacade.createCategory(request, categoryName);
//
//    userFacade.saveCategory(category);
//
//    return ResultResponse.of(SuccessResultCode.SUCCESS_CREATE_CATEGORY);
//  }
//
//  // 카테고리 수정
//  @Override
//  @Transactional
//  public ResultResponse updateCategory(UpdateCategoryDTO updateCategoryDTO, HttpServletRequest request) {
//    CategoryEntity category = userFacade.updateCategory(request, updateCategoryDTO);
//
//    userFacade.saveCategory(category);
//
//    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_CATEGORY);
//  }
//
//
//  // 카테고리 삭제
//  @Override
//  @Transactional
//  public ResultResponse deleteCategory(String categoryName, HttpServletRequest request) {
//    userFacade.deleteCategory(request, categoryName);
//
//    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_CATEGORY);
//  }
//}