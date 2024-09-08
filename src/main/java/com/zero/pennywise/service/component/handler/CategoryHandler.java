package com.zero.pennywise.service.component.handler;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.repository.CategoriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryHandler {

  private final CategoriesRepository categoriesRepository;

  // category 테이블에 존재하지 않은 새로운 카테고리 일 경우
  public void existsCategory(Long userId, String categoryName) {
    if (categoriesRepository.existsByUserIdAndCategoryName(userId, categoryName)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다.");
    }
  }

}
