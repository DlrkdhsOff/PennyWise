package com.zero.pennywise.repository.querydsl.category;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.model.response.Categories;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryQueryRepository {

  List<Categories> getAllCategory(Long userId, Pageable page);

  void updateCategory(Long userId, Long categoryId, Long newCategoryId);
}