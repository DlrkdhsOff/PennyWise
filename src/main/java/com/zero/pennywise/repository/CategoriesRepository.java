package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.CategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long> {

  List<CategoriesEntity> findAllByShared(boolean Shared);

  Optional<CategoriesEntity> findByCategoryName(String categoryName);
}
