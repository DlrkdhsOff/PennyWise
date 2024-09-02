package com.zero.pennywise.service;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.querydsl.budget.BudgetQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public abstract class  CommonService {

  private final UserRepository userRepository;
  private final CategoriesRepository categoriesRepository;
  private final BudgetRepository budgetRepository;
  private final BudgetQueryRepository budgetQueryRepository;

  // 사용자 조회
  UserEntity getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));
  }

  // 카테고리 조회
  CategoriesEntity getCategoryByName(String categoryName) {
    return categoriesRepository.findByCategoryName(categoryName)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다."));
  }

}
