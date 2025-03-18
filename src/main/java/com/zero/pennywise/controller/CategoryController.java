package com.zero.pennywise.controller;

import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 카테고리 관련 작업을 처리하는 REST 컨트롤러.
 * 카테고리 조회, 생성, 삭제 엔드포인트를 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

  // 카테고리 관련 비즈니스 로직을 처리하는 서비스 계층
  private final CategoryService categoryService;

  /**
   * 현재 사용자의 카테고리 목록을 조회합니다.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @return 카테고리 목록과 적절한 HTTP 상태를 포함한 ResponseEntity
   */
  @GetMapping
  public ResponseEntity<ResultResponse> getCategoryList(HttpServletRequest request) {
    ResultResponse response = categoryService.getCategoryList(request);
    return new ResponseEntity<>(response, response.getStatus());
  }

  /**
   * 현재 사용자를 위한 새로운 카테고리를 생성합니다.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param categoryName 생성할 카테고리의 이름
   * @return 카테고리 생성 결과와 적절한 HTTP 상태를 포함한 ResponseEntity
   */
  @PostMapping
  public ResponseEntity<ResultResponse> createCategory(
      HttpServletRequest request,
      @RequestParam("categoryName") String categoryName) {

    ResultResponse resultResponse = categoryService.createCategory(request, categoryName);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  /**
   * 현재 사용자의 기존 카테고리를 삭제합니다.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param categoryName 삭제할 카테고리의 이름
   * @return 카테고리 삭제 결과와 적절한 HTTP 상태를 포함한 ResponseEntity
   */
  @DeleteMapping
  public ResponseEntity<ResultResponse> deleteCategory(
      HttpServletRequest request,
      @RequestParam("categoryName") String categoryName) {

    ResultResponse resultResponse = categoryService.deleteCategory(request, categoryName);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }
}