package com.zero.pennywise.controller;

import com.zero.pennywise.model.dto.BudgetDTO;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.service.BudgetService;
import com.zero.pennywise.service.BudgetTrackerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BudgetController {

  private final BudgetService budgetService;

  // 카테고리별 예산 설정
  @PatchMapping("/budgets")
  public ResponseEntity<?> setBudget(@RequestBody @Valid BudgetDTO BudgetDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 해주세요");
    }

    Response result = budgetService.setBudget(userId, BudgetDTO);

    return ResponseEntity.status(result.getStatus()).body(result.getMessage());
  }

}
