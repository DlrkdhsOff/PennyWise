package com.zero.pennywise.domain.repository;

import com.zero.pennywise.domain.entity.CategoryEntity;
import com.zero.pennywise.domain.entity.UserEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

  // 사용자의 모든 카테고리 조회
  Optional<List<CategoryEntity>> findAllByUser(UserEntity user);

  boolean existsByUserAndCategoryName(UserEntity user, String categoryName);

  @Transactional
  void deleteByUserAndCategoryName(UserEntity user, String categoryName);

  Optional<CategoryEntity> findByUserAndCategoryName(UserEntity user, String categoryName);

}
