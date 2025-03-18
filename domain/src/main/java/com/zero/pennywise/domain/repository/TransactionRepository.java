package com.zero.pennywise.domain.repository;

import com.zero.pennywise.domain.entity.TransactionEntity;
import com.zero.pennywise.domain.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

  void deleteByUserAndTransactionId(UserEntity user, Long trasactionId);

  Optional<TransactionEntity> findByUserAndTransactionId(UserEntity user, Long transactionId);
}
