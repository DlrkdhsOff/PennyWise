package com.zero.pennywise.repository;

import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

  // 사용자의 모든 카테고리 조회
  Optional<List<CategoryEntity>> findAllByUser(UserEntity user);

  // 카테고리 중복 여부 검증
  boolean existsByUserAndCategoryName(UserEntity user, String categoryName);

  Optional<CategoryEntity> findByUserAndCategoryName(UserEntity user, String categoryName);

  Optional<CategoryEntity> findByUserIdAndCategoryId(Long userId, Long cateogryId);


  void deleteByUserIdAndCategoryName(Long userId, String categoryName);

  List<CategoryEntity> findAllByUserId(Long userId);

  void deleteAllByUserId(Long userId);

}
