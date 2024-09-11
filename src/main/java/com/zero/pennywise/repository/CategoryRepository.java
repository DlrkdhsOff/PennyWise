package com.zero.pennywise.repository;

import com.zero.pennywise.entity.CategoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

  Optional<CategoryEntity> findByUserIdAndCategoryName(Long userId, String categoryName);

  Optional<CategoryEntity> findByUserIdAndCategoryId(Long userId, Long cateogryId);

  boolean existsByUserIdAndCategoryName(Long userId, String categoryName);

  void deleteByUserIdAndCategoryName(Long userId, String categoryName);

  List<CategoryEntity> findAllByUserId(Long userId);

  void deleteAllByUserId(Long userId);
}
