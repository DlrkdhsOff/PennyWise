package com.zero.pennywise.repository;

import com.zero.pennywise.entity.TransactionEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
  void deleteAllByUserId(Long userId);

  Optional<TransactionEntity> findByTransactionId(Long transactionId);

  void deleteByUserIdAndTransactionId(Long userId, Long transactionId);

  @Query("SELECT t FROM transactions t WHERE t.dateTime BETWEEN :startDay AND :endDay " +
      "AND (t.type = 'FIXED_INCOME' OR t.type = 'FIXED_EXPENSES')")
  Page<TransactionEntity> findByLastMonthTransaction(
      @Param("startDay") LocalDateTime startDay,
      @Param("endDay") LocalDateTime endDay,
      Pageable pageable);

}
