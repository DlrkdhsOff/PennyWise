package com.zero.pennywise.repository;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

  boolean existsByUserAndCategory(UserEntity user, CategoryEntity category);

  Optional<BudgetEntity> findByBudgetIdAndUser(Long budgetId, UserEntity user);

  void deleteAllByUser(UserEntity user);
}
