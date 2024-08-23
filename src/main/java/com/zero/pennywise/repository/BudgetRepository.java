package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.BudgetEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

  List<BudgetEntity> findAllByUserId(Long userId);

  boolean existsByUserIdAndCategoryId(Long userId, Long categoryId);

  List<BudgetEntity> findAllByUserIdAndCategoryId(Long userId, Long categoryId);
}
