package com.zero.pennywise.repository;

import com.zero.pennywise.entity.CategoriesEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long> {

  Optional<CategoriesEntity> findByUserIdAndCategoryName(Long userId, String categoryName);

  Optional<CategoriesEntity> findByUserIdAndCategoryId(Long userId, Long cateogryId);

  boolean existsByUserIdAndCategoryName(Long userId, String categoryName);

  void deleteByUserIdAndCategoryName(Long userId, String categoryName);

  List<CategoriesEntity> findAllByUserId(Long userId);

  void deleteAllByUserId(Long userId);

  CategoriesEntity findByCategoryId(Long categoryId);
}
