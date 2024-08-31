package com.zero.pennywise.controller;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.budget.BudgetDTO;
import com.zero.pennywise.service.BudgetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BudgetController {

  private final BudgetService budgetService;

  // 카테고리별 예산 설정
  @PostMapping("/budgets")
  public ResponseEntity<?> setBudget(@RequestBody @Valid BudgetDTO BudgetDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(budgetService.setBudget(userId, BudgetDTO));
  }

  // 카테고리별 예산 수정
  @PatchMapping("/budgets")
  public ResponseEntity<?> updateBudget(@RequestBody @Valid BudgetDTO BudgetDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(budgetService.updateBudget(userId, BudgetDTO));
  }

  @GetMapping("/budgets")
  public ResponseEntity<?> getbudget(
      @PageableDefault(page = 0, size = 10) Pageable page,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(budgetService.getBudget(userId, page));
  }
}
