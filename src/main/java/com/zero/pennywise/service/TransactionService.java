package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.UpdateTransactionDTO;
import com.zero.pennywise.model.response.TransactionPage;
import com.zero.pennywise.model.response.TransactionsDTO;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import com.zero.pennywise.service.component.handler.TransactionHandler;
import com.zero.pennywise.service.component.handler.UserHandler;
import com.zero.pennywise.service.component.redis.CategoryCache;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final CategoryCache categoryCache;
  private final UserHandler userHandler;
  private final TransactionHandler transactionHandler;

  // 수입/지출 등록
  public String transaction(Long userId, TransactionDTO transactionDTO) {
    UserEntity user = userHandler.getUserById(userId);
    CategoriesEntity category = categoryCache.getCategoryByCategoryName(userId,
        transactionDTO.getCategoryName());

    transactionRepository.save(TransactionDTO.of(user, category.getCategoryId(), transactionDTO));
    transactionHandler.updateBalance(user.getId(), transactionDTO, category.getCategoryName());

    return "성공적으로 거래를 등록하였습니다.";
  }

  // 수입/지출 내역 조회
  public TransactionPage getTransactionList(Long userId, String categoryName, Pageable page) {
    UserEntity user = userHandler.getUserById(userId);
    Pageable pageable = page(page);

    TransactionPage transactions = StringUtils.hasText(categoryName)
        ? TransactionsDTO.of(
        transactionQueryRepository.getTransactionsByCategory(user, categoryName, pageable))
        : TransactionsDTO.of(transactionQueryRepository.getAllTransaction(user, pageable));

    transactionHandler.validateTransactions(transactions.getTransactions(), categoryName);

    return transactions;
  }

  // 매일 00시 고정 수입/지출 자동 등록
  public void updateFixedTransaction() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String todayDate = LocalDateTime.now().format(formatter);  // 오늘 날짜
    String lastMonthsDate = transactionHandler.getLastMonthsDate();  // 한 달 전 날짜

    List<TransactionEntity> transactions = transactionRepository.findByDateTimeStartingWith(
        lastMonthsDate);
    for (TransactionEntity transaction : transactions) {
      transactionRepository.save(TransactionEntity.builder()
          .user(transaction.getUser())
          .categoryId(transaction.getCategoryId())
          .type(transaction.getType())
          .amount(transaction.getAmount())
          .description(transaction.getDescription())
          .dateTime(todayDate)
          .build());
    }
  }

  // 거래 정보 수정
  public String updateTransaction(Long userId, UpdateTransactionDTO updateTransaction) {
    UserEntity user = userHandler.getUserById(userId);
    TransactionEntity transaction = transactionHandler.getTransaction(
        updateTransaction.getTransactionId());

    CategoriesEntity newCategory = categoryCache.getCategoryByCategoryName(user.getId(),
        updateTransaction.getCategoryName());

    transactionHandler.updateBalanceCacheData(userId, transaction, updateTransaction);
    transactionHandler.updateTransactionDetails(transaction, newCategory, updateTransaction);

    transactionRepository.save(transaction);
    return "거래 정보를 수정하였습니다.";
  }

  // 거래 삭제
  public String deleteTransaction(Long userId, Long transactionId) {
    UserEntity user = userHandler.getUserById(userId);
    TransactionEntity transaction = transactionHandler.getTransaction(transactionId);

    transactionHandler.deleteBalanceCacheData(userId, transaction);
    transactionRepository.deleteByUserIdAndTransactionId(user.getId(),
        transaction.getTransactionId());

    return "거래를 성공적으로 삭제하였습니다.";
  }
}