package com.zero.pennywise.controller;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.model.request.category.CategoryDTO;
import com.zero.pennywise.model.request.category.UpdateCategoryDTO;
import com.zero.pennywise.model.response.category.CategoriesPage;
import com.zero.pennywise.service.CategoryService;
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
public class CategoryController {

  private final CategoryService categoryService;

  // 카테고리 목록 출력
  @GetMapping("/categories")
  public ResponseEntity<CategoriesPage> category( @PageableDefault(page = 0, size = 10) Pageable page) {

    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.ok(categoryService.getCategoryList(userId, page(page)));
  }


  // 카테고리 생성
  @PostMapping("/categories")
  public ResponseEntity<String> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {

    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(categoryService.createCategory(userId, categoryDTO.getCategoryName()));
  }

  // 카테고리 생성
  @PatchMapping("/categories")
  public ResponseEntity<String> updateCategory(@RequestBody @Valid UpdateCategoryDTO updateCategory) {

    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(categoryService.updateCategory(userId, updateCategory));
  }


  // 카테고리 삭제
  @DeleteMapping("/categories")
  public ResponseEntity<String> deleteCategory(@RequestBody @Valid CategoryDTO categoryDTO) {

    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.ok()
        .body(categoryService.deleteCategory(userId, categoryDTO.getCategoryName()));
  }
}
