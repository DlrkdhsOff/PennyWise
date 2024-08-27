package com.zero.pennywise.repository.budget;

import com.zero.pennywise.model.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BudgetRepository extends JpaRepository<BudgetEntity, Long>
    , BudgetQueryRepository {

  boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);

  BudgetEntity findByUserIdAndCategoryId(Long userId, Long categoryId);
}
