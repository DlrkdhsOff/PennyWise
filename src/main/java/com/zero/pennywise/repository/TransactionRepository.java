package com.zero.pennywise.repository;

import com.zero.pennywise.entity.TransactionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
  void deleteAllByUserId(Long userId);

  boolean existsByUserId(Long userId);

  Optional<TransactionEntity> findByTransactionId(Long transactionId);

  List<TransactionEntity> findByDateTimeStartingWith(String lastMonthsDate);

  void deleteByUserIdAndTransactionId(Long userId, Long transactionId);
}
