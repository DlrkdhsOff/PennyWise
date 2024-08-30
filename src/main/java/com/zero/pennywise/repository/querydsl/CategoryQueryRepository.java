package com.zero.pennywise.repository.querydsl;

import com.zero.pennywise.model.entity.CategoriesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryQueryRepository {

  Page<String> getAllCategory(Long userId, Pageable page);

  void updateCategory(Long userId, Long categoryId, CategoriesEntity newCategory);
}