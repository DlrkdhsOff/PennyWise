package com.zero.pennywise.repository;

import com.zero.pennywise.entity.BudgetEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

  void deleteAllByUserId(Long userId);

  boolean existsByUserIdAndCategoryCategoryId(Long userId, Long categoryId);

  Optional<BudgetEntity> findByUserIdAndCategoryCategoryId(Long userId, Long categoryId);

  void deleteByBudgetId(Long budgetId);

  List<BudgetEntity> findAllByUserId(Long useId);
}
