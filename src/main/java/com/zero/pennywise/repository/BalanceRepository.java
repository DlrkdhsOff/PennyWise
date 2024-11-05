package com.zero.pennywise.repository;

import com.zero.pennywise.entity.BalanceEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {


  Optional<BalanceEntity> findByUserAndCategoryAndRecordMonth(UserEntity user, CategoryEntity category, String recordMonth);

  void deleteAllByUser(UserEntity user);
}
