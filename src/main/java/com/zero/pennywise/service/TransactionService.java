package com.zero.pennywise.service;

import com.zero.pennywise.component.handler.CategoryHandler;
import com.zero.pennywise.component.handler.TransactionHandler;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.UpdateTransactionDTO;
import com.zero.pennywise.model.response.transaction.TransactionsDTO;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final UserHandler userHandler;
  private final CategoryHandler categoryHandler;
  private final TransactionHandler transactionHandler;

  // 수입/지출 등록
  public String transaction(Long userId, TransactionDTO transactionDTO) {
    UserEntity user = userHandler.getUserById(userId);

    CategoryEntity category = categoryHandler
        .getCateogry(user.getId(), transactionDTO.getCategoryName());

    return transactionHandler.addTransaction(user, category, transactionDTO);
  }

  // 수입/지출 내역 조회
  public TransactionPage getTransactionList(Long userId, String categoryName, Pageable page) {
    UserEntity user = userHandler.getUserById(userId);

    TransactionPage transactions = TransactionsDTO.of(transactionQueryRepository
        .getAllTransaction(user, categoryName,  page));

    transactionHandler.validateTransactions(transactions.getTransactions(), categoryName);

    return transactions;
  }

  // 거래 정보 수정
  public String updateTransaction(Long userId, UpdateTransactionDTO updateTransaction) {
    UserEntity user = userHandler.getUserById(userId);

    TransactionEntity transaction = transactionHandler
        .getTransaction(updateTransaction.getTransactionId());

    transactionHandler.updateRedisBalance(user, transaction, updateTransaction);
    transactionHandler.updateTransactionDetails(user, transaction, updateTransaction);
    return "거래 정보를 수정하였습니다.";
  }


  // 거래 삭제
  @Transactional
  public String deleteTransaction(Long userId, Long transactionId) {
    UserEntity user = userHandler.getUserById(userId);
    TransactionEntity transaction = transactionHandler.getTransaction(transactionId);

    transactionHandler.restoration(user, transaction);
    transactionRepository.deleteByUserIdAndTransactionId(user.getId(), transaction.getTransactionId());

    return "거래를 성공적으로 삭제하였습니다.";
  }

}