package com.zero.pennywise.repository;

import com.zero.pennywise.model.entity.TransactionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

  boolean existsByUserId(Long userId);

  Optional<TransactionEntity> findByTransactionId(Long transactionId);
}
