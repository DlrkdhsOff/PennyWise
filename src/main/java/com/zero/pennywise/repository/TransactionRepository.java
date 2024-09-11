package com.zero.pennywise.repository;

import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.model.response.transaction.EndSavindDTO;
import com.zero.pennywise.status.TransactionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

  @Query("SELECT t "
      + "FROM transactions t "
      + "WHERE t.dateTime BETWEEN :startDay AND :endDay " +
      "AND (t.type = 'FIXED_INCOME' OR t.type = 'FIXED_EXPENSES')")
  Page<TransactionEntity> findByLastMonthTransaction(
      @Param("startDay") LocalDateTime startDay,
      @Param("endDay") LocalDateTime endDay,
      Pageable pageable);

  @Query("SELECT t "
      + "FROM transactions t "
      + "JOIN savings s "
      + "ON t.categoryId = s.category.categoryId "
      + "WHERE s.endDate = :endDate")
  Page<TransactionEntity> findByEndDate(@Param("endDate") LocalDate endDate, Pageable pageable);

  @Query("SELECT new com.zero.pennywise.model.response.transaction.EndSavindDTO("
      + "SUM(t.amount), t.user, t.categoryId) "
      + "FROM transactions t "
      + "JOIN savings s "
      + "ON t.categoryId = s.category.categoryId "
      + "WHERE s.endDate = :endDate AND t.type = :type "
      + "GROUP BY t.user, t.categoryId")
  Page<EndSavindDTO> findAmountSumByType(
      @Param("type") TransactionStatus type,
      @Param("endDate") LocalDate endDate,
      Pageable pageable);

}
