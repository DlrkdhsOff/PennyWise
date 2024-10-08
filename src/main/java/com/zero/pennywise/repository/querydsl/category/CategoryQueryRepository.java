package com.zero.pennywise.repository.querydsl.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryQueryRepository {

  Page<String> getAllCategory(Long userId, Pageable pageable);

}