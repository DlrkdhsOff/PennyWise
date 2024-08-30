package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.UserCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategoryEntity, Long> {
  void deleteAllByUserId(Long userId);

  boolean existsByUserIdAndCategoryCategoryId(Long userId, Long categoryId);
}
