package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

  boolean existsByUserIdAndCategoryCategoryId(Long userId, Long categoryId);

  BudgetEntity findByUserIdAndCategoryCategoryId(Long userId, Long categoryId);
//  BudgetEntity findByUserIdAndCategory(Long userId, Long categoryId);
}
