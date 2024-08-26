package com.zero.pennywise.repository.transaction;

import com.zero.pennywise.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>,
    TransactionQueryRepository {

}
