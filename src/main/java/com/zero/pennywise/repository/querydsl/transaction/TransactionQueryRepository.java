package com.zero.pennywise.repository.querydsl.transaction;

import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.transaction.TransactionsDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionQueryRepository {

  Page<TransactionsDTO> getAllTransaction(UserEntity user, String categoryName, Pageable page);

  List<TransactionEntity> findByLastMonthTransaction(String lastMonthsDate);

  Long getTracsactionAvgLastThreeMonth(Long userId, Long categoryId, LocalDateTime startDateTime, LocalDateTime endDateTime);

  Long getExpenses(Long userId, Long categoryId, String thisMonths);
}
