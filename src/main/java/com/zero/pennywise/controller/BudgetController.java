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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
public class BudgetController {

  private final BudgetService budgetService;


  // 예산 목록
  @GetMapping
  public ResponseEntity<ResultResponse> getBudget(
      @RequestParam(value = "categoryName", required = false) String categoryName,
      @RequestParam("page") int page,
      HttpServletRequest request) {

    ResultResponse resultResponse = budgetService.getBudget(categoryName, page, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  // 카테고리별 예산 설정
  @PostMapping
  public ResponseEntity<ResultResponse> createBudget(
      @RequestBody @Valid BudgetDTO BudgetDTO,
      HttpServletRequest request) {

    ResultResponse resultResponse = budgetService.createBudget(BudgetDTO, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  // 카테고리별 예산 수정
  @PutMapping
  public ResponseEntity<ResultResponse> updateBudget(
      @RequestBody @Valid UpdateBudgetDTO updateBudgetDTO,
      HttpServletRequest request) {

    ResultResponse resultResponse = budgetService.updateBudget(updateBudgetDTO, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  // 예산 삭제
  @DeleteMapping
  public ResponseEntity<ResultResponse> deleteBudget(
      @RequestParam("budgetId") Long budgetId) {

    ResultResponse resultResponse = budgetService.deleteBudget(budgetId);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }
}
