package com.zero.pennywise.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

  boolean existsByUserAndCategory(UserEntity user, CategoryEntity category);

  Optional<BudgetEntity> findByUserAndCategory(UserEntity user, CategoryEntity category);

  void deleteByBudgetId(Long budgetId);

  List<BudgetEntity> findAllByUserId(Long useId);

  void deleteAllByUserId(Long userId);
}
