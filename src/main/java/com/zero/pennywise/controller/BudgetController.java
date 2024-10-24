package com.zero.pennywise.controller;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.request.category.CategoryDTO;
import com.zero.pennywise.service.BudgetService;
import com.zero.pennywise.utils.UserAuthorizationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BudgetController {

  private final BudgetService budgetService;

  // 카테고리별 예산 설정
  @PostMapping("/budgets")
  public ResponseEntity<String> setBudget(@RequestBody @Valid BudgetDTO BudgetDTO) {

    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.status(HttpStatus.OK)
        .body(budgetService.setBudget(userId, BudgetDTO));
  }

  // 카테고리별 예산 수정
  @PatchMapping("/budgets")
  public ResponseEntity<String> updateBudget(@RequestBody @Valid BudgetDTO BudgetDTO) {

    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.status(HttpStatus.OK)
        .body(budgetService.updateBudget(userId, BudgetDTO));
  }

  // 예산 목록
  @GetMapping("/budgets")
  public ResponseEntity<BudgetPage> getbudget(@PageableDefault(page = 1, size = 11) Pageable page) {

    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.status(HttpStatus.OK)
        .body(budgetService.getBudget(userId, page(page)));
  }

  // 예산 삭제
  @DeleteMapping("/budgets")
  public ResponseEntity<String> deleteBudget(@RequestBody @Valid CategoryDTO categoryDTO) {
    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.status(HttpStatus.OK)
        .body(budgetService.deleteBudget(userId, categoryDTO.getCategoryName()));
  }
}
