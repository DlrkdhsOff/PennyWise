package com.zero.pennywise.repository;

import com.zero.pennywise.entity.TransactionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
  void deleteAllByUserId(Long userId);

  Optional<TransactionEntity> findByTransactionId(Long transactionId);

  void deleteByUserIdAndTransactionId(Long userId, Long transactionId);

}
