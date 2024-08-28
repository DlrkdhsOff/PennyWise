package com.zero.pennywise.repository.querydsl;

import com.zero.pennywise.model.response.TransactionsDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionQueryRepository {

  List<TransactionsDTO> getAllTransaction(Long userId, String page);

  List<TransactionsDTO> getTransactionsByCategory(Long userId, String categoryName, String page);
}
