package com.zero.pennywise.controller;

import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.request.budget.UpdateBudgetDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.service.BudgetService;
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

/**
 * 예산 관련 REST API 엔드포인트를 제공하는 컨트롤러.
 * 사용자의 예산 목록 조회, 생성, 수정, 삭제 기능을 지원합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
public class BudgetController {

  private final BudgetService budgetService;

  /**
   * 현재 사용자의 모든 예산 목록 조회.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @return 사용자의 예산 목록과 HTTP 상태를 포함한 ResponseEntity
   */
  @GetMapping
  public ResponseEntity<ResultResponse> getBudgetList(HttpServletRequest request) {
    // 예산 목록 조회 서비스 호출
    ResultResponse resultResponse = budgetService.getBudgetList(request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  /**
   * 카테고리별 예산 생성.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param budgetDTO 생성할 예산 정보를 담은 DTO (유효성 검증 포함)
   * @return 예산 생성 결과와 HTTP 상태를 포함한 ResponseEntity
   */
  @PostMapping
  public ResponseEntity<ResultResponse> createBudget(HttpServletRequest request,
      @RequestBody @Valid BudgetDTO budgetDTO) {
    // 예산 생성 서비스 호출
    ResultResponse resultResponse = budgetService.createBudget(request, budgetDTO);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  /**
   * 카테고리별 예산 수정.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param updateBudgetDTO 수정할 예산 정보를 담은 DTO (유효성 검증 포함)
   * @return 예산 수정 결과와 HTTP 상태를 포함한 ResponseEntity
   */
  @PutMapping
  public ResponseEntity<ResultResponse> updateBudget(HttpServletRequest request,
      @RequestBody @Valid BudgetDTO budgetDTO) {
    // 예산 수정 서비스 호출
    ResultResponse resultResponse = budgetService.updateBudget(request, budgetDTO);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  /**
   * 카테고리별 예산 삭제.
   *
   * @param request 사용자 인증 정보를 포함하는 HTTP 서블릿 요청
   * @param categoryName 삭제할 예산의 카테고리 이름
   * @return 예산 삭제 결과와 HTTP 상태를 포함한 ResponseEntity
   */
  @DeleteMapping
  public ResponseEntity<ResultResponse> deleteBudget(HttpServletRequest request,
      @RequestParam("categoryName") String categoryName) {
    // 예산 삭제 서비스 호출
    ResultResponse resultResponse = budgetService.deleteBudget(request, categoryName);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }
}