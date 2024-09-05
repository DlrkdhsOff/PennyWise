package com.zero.pennywise.repository.querydsl.budget;

import org.springframework.stereotype.Repository;

@Repository
public interface BudgetQueryRepository {

  void updateCategory(Long userId, Long categoryId, Long newCategoryId);
}