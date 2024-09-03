package com.zero.pennywise.repository.querydsl.budget;

import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.entity.CategoriesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetQueryRepository {

  Page<BudgetDTO> findAllBudgetByUserId(Long userId, Pageable pageable);

  void updateCategoryId(Long useId, Long categoryId, CategoriesEntity updatedCategory);
}