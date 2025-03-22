package com.zero.pennywise.domain.repository;

import com.zero.pennywise.domain.entity.BudgetEntity;
import com.zero.pennywise.domain.entity.CategoryEntity;
import com.zero.pennywise.domain.entity.UserEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

  Optional<List<BudgetEntity>> findAllByUser(UserEntity user);

  boolean existsByUserAndCategory(UserEntity user, CategoryEntity category);

  Optional<BudgetEntity> findByUserAndCategory(UserEntity user, CategoryEntity category);

  @Modifying
  @Transactional
  @Query("UPDATE budgets b SET b.amount = :amount WHERE b.budgetId = :budgetId")
  void updateBudget(Long budgetId, Long amount);
}
