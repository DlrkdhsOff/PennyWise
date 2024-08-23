package com.zero.pennywise.controller;

import com.zero.pennywise.model.dto.CategoryDTO;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.service.BudgetTrackerService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/budgets")
public class BudgetTrackerController {

  private final BudgetTrackerService budgetTrackerService;

  @GetMapping("/categories")
  public ResponseEntity<?> category(HttpServletRequest request) {
    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 해주세요");
    }

    List<CategoriesEntity> categoryList = budgetTrackerService.getCategoryList(userId);

    return ResponseEntity.ok(categoryList);
  }
}
