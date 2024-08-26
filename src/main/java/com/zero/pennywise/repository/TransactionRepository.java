package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.TransactionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

  List<TransactionEntity> findAllByUserIdAndCategoryId(Long userId, Long categoryId);

  List<TransactionEntity> findAllByUserId(Long userId);
}
