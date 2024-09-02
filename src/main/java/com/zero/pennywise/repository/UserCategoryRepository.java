package com.zero.pennywise.repository;

import com.zero.pennywise.entity.UserCategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategoryEntity, Long> {
  void deleteAllByUserId(Long userId);

  void deleteAllByUserIdAndCategoryCategoryId(Long userId, Long categoryId);

  boolean existsByUserIdAndCategoryCategoryId(Long userId, Long categoryId);

  boolean existsByUserNotAndCategoryCategoryId(UserEntity user, Long categoryId);
}
