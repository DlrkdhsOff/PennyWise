package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.UserCategoryEntity;
import com.zero.pennywise.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategoryEntity, Long> {
  void deleteAllByUserId(Long userId);

  boolean existsByUserIdAndCategoryCategoryId(Long userId, Long categoryId);

  boolean existsByUserNotAndCategoryCategoryId(UserEntity user, Long categoryId);
}
