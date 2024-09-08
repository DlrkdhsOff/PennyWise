package com.zero.pennywise.repository.querydsl.category;

import com.zero.pennywise.entity.CategoriesEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryQueryRepository {

  List<CategoriesEntity> getAllCategory(Long userId);

}