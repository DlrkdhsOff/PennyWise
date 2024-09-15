package com.zero.pennywise.component.handler;

import static com.zero.pennywise.status.TransactionStatus.getEnumType;

import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.UpdateTransactionDTO;
import com.zero.pennywise.model.response.transaction.TransactionsDTO;
import com.zero.pennywise.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TransactionHandler {

  private final RedisHandler redisHandler;
  private final TransactionRepository transactionRepository;
  private final CategoryHandler categoryHandler;


  // 거래 등록
  public String addTransaction(UserEntity user, CategoryEntity category, TransactionDTO transactionDTO) {
    TransactionEntity transaction = transactionRepository
        .save(TransactionDTO.of(user, category.getCategoryId(), transactionDTO));

    if (transaction.getType().isExpenses()) {
      redisHandler.updateBalance(user, transaction.getAmount(), category.getCategoryName());
    }

    return "성공적으로 거래를 등록하였습니다.";
  }


  // 거래 목록 조회 유효값 검증
  public void validateTransactions(List<TransactionsDTO> transactions, String categoryName) {
    if (transactions == null || transactions.isEmpty()) {
      String message =
          StringUtils.hasText(categoryName) ? "존재하지 않은 카테고리 입니다." : "거래 내역에 존재하지 않습니다.";
      throw new GlobalException(HttpStatus.BAD_REQUEST, message);
    }
  }


  // 입력한 데이터 수정
  @Transactional
  public void updateTransactionDetails(UserEntity user, TransactionEntity transaction,
      UpdateTransactionDTO updateTransaction) {

    CategoryEntity category = categoryHandler.getCateogry(user.getId(),
        updateTransaction.getCategoryName());

    transaction.setCategoryId(category.getCategoryId());
    transaction.setType(getEnumType(updateTransaction.getType()));
    transaction.setAmount(updateTransaction.getAmount());
    transaction.setDescription(updateTransaction.getDescription());

    transactionRepository.save(transaction);
  }


  // 거래 조회
  public TransactionEntity getTransaction(Long transactionId) {
    return transactionRepository.findByTransactionId(transactionId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 거래 아이디 입니다."));
  }


  // Redis balance data 수정
  @Transactional
  public void updateRedisBalance(UserEntity user, TransactionEntity transaction,
      UpdateTransactionDTO updateTransaction) {

    restoration(user, transaction);

    // 수정한 금액으로 남은 예산 수정
    if (getEnumType(updateTransaction.getType()).isExpenses()) {
      redisHandler.updateBalance(user, updateTransaction.getAmount(), updateTransaction.getCategoryName());
    }
  }

  // 남은 예산 금액 원상 복구
  public void restoration(UserEntity user, TransactionEntity transaction) {
    CategoryEntity category = categoryHandler
        .getCateogryByUserIdAndId(user.getId(), transaction.getCategoryId());

    if (transaction.getType().isExpenses()) {
      redisHandler.updateBalance(user, transaction.getAmount() * -1, category.getCategoryName());
    }
  }

}