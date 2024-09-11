package com.zero.pennywise.repository.querydsl.transaction;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.transaction.TransactionsDTO;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionQueryRepository {

  Page<TransactionsDTO> getAllTransaction(UserEntity user, String categoryName, Pageable page);

  Long getExpensesAvgLastThreeMonth(Long userId, Long categoryId, LocalDateTime startDateTime, LocalDateTime endDateTime);

  Long getExpenses(Long userId, Long categoryId, String thisMonths);

  Long getCurrentAmount(UserEntity user, Long categoryId, String description);

  void endSavings(Long userId, Long categoryId, String description);

  Long getFixedIncomeAvgLastThreeMonth(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
