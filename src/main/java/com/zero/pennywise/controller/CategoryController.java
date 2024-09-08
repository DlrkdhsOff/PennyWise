package com.zero.pennywise.controller;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.category.CategoryDTO;
import com.zero.pennywise.model.response.category.CategoriesPage;
import com.zero.pennywise.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CategoryController {

  private final CategoryService categoryService;

  // 카테고리 목록 출력
  @GetMapping("/categories")
  public ResponseEntity<CategoriesPage> category(HttpServletRequest request,
      @PageableDefault(page = 0, size = 10) Pageable page) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    Pageable pageable = page(page);
    return ResponseEntity.ok(categoryService.getCategoryList(userId, pageable));
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
        .body(categoryService.createCategory(userId, categoryDTO.getCategoryName()));
  }


  // 카테고리 삭제
  @DeleteMapping("/categories")
  public ResponseEntity<String> deleteCategory(@RequestBody @Valid CategoryDTO categoryDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.ok()
        .body(categoryService.deleteCategory(userId, categoryDTO.getCategoryName()));
  }
}
