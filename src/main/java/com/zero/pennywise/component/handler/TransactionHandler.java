package com.zero.pennywise.component.handler;

import static com.zero.pennywise.status.TransactionStatus.castToTransactionStatus;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.entity.WaringMessageEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.budget.BalancesDTO;
import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.UpdateTransactionDTO;
import com.zero.pennywise.model.response.transaction.TransactionsDTO;
import com.zero.pennywise.model.response.waring.WaringMessageDTO;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.WaringMessageRepository;
import com.zero.pennywise.component.cache.BudgetCache;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TransactionHandler {

  private static final String EXPENSE = "지출";

  private final BudgetCache budgetCache;
  private final TransactionRepository transactionRepository;
  private final RedisTemplate<String, Object> redisTemplate;
  private final WaringMessageRepository waringMessageRepository;
  private final CategoryHandler categoryHandler;


  // 거래 등록
  public String addTransaction(UserEntity user, CategoriesEntity category, TransactionDTO transactionDTO) {
    TransactionEntity transaction = transactionRepository
        .save(TransactionDTO.of(user, category.getCategoryId(), transactionDTO));

    addBalance(user, transaction, category.getCategoryName());

    return "성공적으로 거래를 등록하였습니다.";
  }


  // 입력한 데이터 수정
  public String updateTransactionDetails(UserEntity user, TransactionEntity transaction, UpdateTransactionDTO updateTransaction) {
    CategoriesEntity category = categoryHandler
        .getCateogry(user.getId(), updateTransaction.getCategoryName());

    transaction.setCategoryId(category.getCategoryId());
    transaction.setType(castToTransactionStatus(updateTransaction.getType(), updateTransaction.getIsFixed()));
    transaction.setAmount(updateTransaction.getAmount());
    transaction.setDescription(updateTransaction.getDescription());

    transactionRepository.save(transaction);
    return "거래 정보를 수정하였습니다.";
  }


  // 거래 목록 유효값 검증
  public void validateTransactions(List<TransactionsDTO> transactions, String categoryName) {
    if (transactions == null || transactions.isEmpty()) {
      String message =
          StringUtils.hasText(categoryName) ? "존재하지 않은 카테고리 입니다." : "거래 내역에 존재하지 않습니다.";
      throw new GlobalException(HttpStatus.BAD_REQUEST, message);
    }
  }


  // 거래 조회
  public TransactionEntity getTransaction(Long transactionId) {
    return transactionRepository.findByTransactionId(transactionId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 거래 아이디 입니다."));
  }


  // 잔액 업데이트 필요시 처리
  public void addBalance(UserEntity user, TransactionEntity transaction, String categoryName) {
    BalancesDTO balance = budgetCache.getBalances(user.getId(), categoryName);

    if (balance == null) {
      return;
    }

    if (transaction.getType().isExpenses()) {
      long totalBalance = calculateBalance(user, balance, transaction.getAmount(),
          categoryName);
      balance.setBalance(totalBalance);
    }

    budgetCache.updateBalance(user.getId(), balance.getBalance(), categoryName);
  }


  // 남은 예산 금액 계산
  private long calculateBalance(UserEntity user, BalancesDTO balance, Long amount, String categoryName) {
    long totalBalance = balance.getBalance() - amount;

    if (totalBalance < 0) {
      sendMessage(user, categoryName + " 예산을 초과 했습니다.");
      totalBalance = 0L;
    }

    return totalBalance;
  }

  // 경고 메시지 전송
  public void sendMessage(UserEntity user, String message) {
    WaringMessageEntity warningMessage = WaringMessageEntity.builder()
        .user(user)
        .message(message)
        .recivedDateTime(LocalDateTime.now())
        .build();

    waringMessageRepository.save(warningMessage);
    redisTemplate.convertAndSend("notifications", new WaringMessageDTO(user.getId(), message));
  }


  // 캐시에 저장 되어 있는 예산 수정
  public void updateBalanceCacheData(UserEntity user, TransactionEntity transaction, UpdateTransactionDTO updateTransaction) {
    CategoriesEntity beforeCategory = categoryHandler
        .getCateogryById(user.getId(), transaction.getCategoryId());

    BalancesDTO balance = budgetCache.getBalances(user.getId(), beforeCategory.getCategoryName());

    // 캐시 데이터 원상 복구
    if (transaction.getType().isExpenses()) {
      balance.setBalance(balance.getBalance() + transaction.getAmount());
    }

    boolean isSameCategory = beforeCategory
        .getCategoryName().equals(updateTransaction.getCategoryName());

    if (isSameCategory) {
      updateBalanceForSameCategory(user, balance, updateTransaction);
    } else {
      updateBalanceForNewCategory(user, updateTransaction);
    }
  }


  // 같은 카테고리를 수정할 경우
  private void updateBalanceForSameCategory(UserEntity user, BalancesDTO balance, UpdateTransactionDTO updateTransaction) {
    if (isExpense(updateTransaction.getType())) {

      long totalBalance = calculateBalance(user, balance,
          updateTransaction.getAmount(), balance.getCategoryName());

      balance.setBalance(totalBalance);
    }
    budgetCache.updateBalance(user.getId(), balance.getBalance(), balance.getCategoryName());
  }

  // 다른 카테고리로 수정하는 경우
  private void updateBalanceForNewCategory(UserEntity user, UpdateTransactionDTO updateTransaction) {
    CategoriesEntity newCategory = categoryHandler
        .getCateogry(user.getId(), updateTransaction.getCategoryName());

    BalancesDTO balance = budgetCache.getBalances(user.getId(), newCategory.getCategoryName());

    if (isExpense(updateTransaction.getType())) {
      long totalBalance = calculateBalance(user, balance,
          updateTransaction.getAmount(),
          newCategory.getCategoryName());

      balance.setBalance(totalBalance);
    }

    budgetCache.updateBalance(user.getId(), balance.getBalance(), balance.getCategoryName());
  }

  // 타입 확인
  private boolean isExpense(String type) {
    return EXPENSE.equals(type);
  }

  // 거래 삭제시 남은 금액 변경
  public void deleteBalanceCacheData(Long userId, TransactionEntity transaction) {
    if (!transaction.getType().isExpenses()) {
      return;
    }
    CategoriesEntity beforeCategory = categoryHandler
        .getCateogryById(userId, transaction.getCategoryId());

    BalancesDTO balance = budgetCache.getBalances(userId, beforeCategory.getCategoryName());
    balance.setBalance(balance.getBalance() + transaction.getAmount());

    budgetCache.updateBalance(userId, balance.getBalance(), balance.getCategoryName());
  }

}