package com.zero.pennywise.repository;

import com.zero.pennywise.entity.BalanceEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {

  // 지정한 달의 사용자와 카테고리가 일치하는 데이터 반환
  Optional<BalanceEntity> findByUserAndCategoryAndRecordMonth(UserEntity user, CategoryEntity category, String recordMonth);

  // 사용자의 모든 데이터를 삭제
  void deleteAllByUser(UserEntity user);
}
