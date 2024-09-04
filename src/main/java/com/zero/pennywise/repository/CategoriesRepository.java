package com.zero.pennywise.repository;

import com.zero.pennywise.entity.CategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long> {

  CategoriesEntity findByCategoryName(String categoryName);

  void deleteByCategoryId(Long categoryId);
}
