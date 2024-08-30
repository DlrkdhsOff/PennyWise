package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.UserCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategoryEntity, Long> {

  boolean existsByUserIdAndCategoryCategoryId(Long userId, Long categoryId);
}