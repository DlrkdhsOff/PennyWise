package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.CategoriesEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Long> {

  List<CategoriesEntity> findAllBySharedIsTrue();

  Optional<CategoriesEntity> findByCategoryName(String categoryName);
}
