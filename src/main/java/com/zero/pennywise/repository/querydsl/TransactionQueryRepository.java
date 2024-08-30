package com.zero.pennywise.repository.querydsl;

import com.zero.pennywise.model.entity.TransactionEntity;
import com.zero.pennywise.model.entity.UserEntity;
import com.zero.pennywise.model.response.TransactionsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface TransactionQueryRepository {

  Page<TransactionsDTO> getAllTransaction(UserEntity user, Pageable page);

  Page<TransactionsDTO> getTransactionsByCategory(UserEntity user, String categoryName, Pageable page);

  List<TransactionEntity> findFixedTransaction(String lastMonthsDate);

}
