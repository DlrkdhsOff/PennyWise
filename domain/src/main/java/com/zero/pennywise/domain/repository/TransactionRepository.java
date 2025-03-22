package com.zero.pennywise.domain.repository;

import com.zero.pennywise.domain.entity.TransactionEntity;
import com.zero.pennywise.domain.entity.UserEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

  Optional<TransactionEntity> findByUserAndTransactionId(UserEntity user, Long transactionId);
}
