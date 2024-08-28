package com.zero.pennywise.controller;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.CategoryDTO;
import com.zero.pennywise.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  // 카테고리 목록 출력
  @GetMapping("/categories")
  public ResponseEntity<?> category(HttpServletRequest request,
      @RequestParam(name = "page", required = false) String page) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.ok(categoryService.getCategoryList(userId, page));
  }

  // 카테고리 생성
  @PostMapping("/categories")
  public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryDTO categoryDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(categoryService.createCategory(userId, categoryDTO));
  }

}
