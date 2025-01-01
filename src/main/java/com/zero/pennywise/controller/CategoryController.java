package com.zero.pennywise.controller;

import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

  private final CategoryService categoryService;

  // 카테고리 목록 출력
  @GetMapping
  public ResponseEntity<ResultResponse> category(
      @RequestParam("page") int page,
      HttpServletRequest request) {

    ResultResponse resultResponse = categoryService.getCategoryList(page, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }


  // 카테고리 생성
  @PostMapping
  public ResponseEntity<ResultResponse> createCategory(
      @RequestParam("categoryName") String categoryName,
      HttpServletRequest request) {

    ResultResponse resultResponse = categoryService.createCategory(categoryName, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }


  // 카테고리 삭제
  @DeleteMapping
  public ResponseEntity<ResultResponse> deleteCategory(
      @RequestParam("categoryName") String categoryName,
      HttpServletRequest request) {


    ResultResponse resultResponse = categoryService.deleteCategory(categoryName, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }
}
