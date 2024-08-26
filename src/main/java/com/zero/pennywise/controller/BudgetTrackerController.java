package com.zero.pennywise.controller;

import com.zero.pennywise.model.dto.BudgetDTO;
import com.zero.pennywise.model.dto.CategoryDTO;
import com.zero.pennywise.model.dto.TransactionDTO;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.service.BudgetTrackerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budgets")
public class BudgetTrackerController {

  private final BudgetTrackerService budgetTrackerService;

  // 카테고리 목록 출력
  @GetMapping("/categories")
  public ResponseEntity<?> category(HttpServletRequest request) {
    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 해주세요");
    }

    List<String> categoryList = budgetTrackerService.getCategoryList(userId);

    return ResponseEntity.ok(categoryList);
  }

  // 카테고리 생성
  @PostMapping("/create-category")
  public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryDTO categoryDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 해주세요");
    }

    Response result = budgetTrackerService.createCategory(userId, categoryDTO);

    return ResponseEntity.status(result.getStatus()).body(result.getMessage());
  }

  // 카테고리별 예산 설정
  @PatchMapping("/set-budget")
  public ResponseEntity<?> setBudget(@RequestBody @Valid BudgetDTO BudgetDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 해주세요");
    }

    Response result = budgetTrackerService.setBudget(userId, BudgetDTO);

    return ResponseEntity.status(result.getStatus()).body(result.getMessage());
  }

  // 수입 / 지출 등록
  @PostMapping("/transaction")
  public ResponseEntity<?> transaction(@RequestBody @Valid TransactionDTO transactionDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 해주세요");
    }

    Response result = budgetTrackerService.transaction(userId, transactionDTO);

    return ResponseEntity.status(result.getStatus()).body(result.getMessage());
  }

  // 거래 목록 출력(전체 / 카테고리별)
  @GetMapping("/transaction-list")
  public ResponseEntity<?> getTransactionList(
      @RequestParam(name = "categoryName", required = false) String categoryName,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 해주세요");
    }

    return ResponseEntity.ok().body(budgetTrackerService.getTransactionList(userId, categoryName));
  }
}
