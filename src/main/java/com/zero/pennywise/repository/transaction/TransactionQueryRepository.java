package com.zero.pennywise.repository.transaction;

import com.zero.pennywise.model.response.TransactionsDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionQueryRepository {

  List<TransactionsDTO> getAllTransaction(Long userId, String page);

  List<TransactionsDTO> getTransactionsByCategory(Long userId, String categoryName, String page);
}
