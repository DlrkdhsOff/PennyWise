package com.zero.pennywise.service.component.handler;

import static com.zero.pennywise.status.TransactionStatus.castToTransactionStatus;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.budget.BalancesDTO;
import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.request.transaction.UpdateTransactionDTO;
import com.zero.pennywise.model.response.TransactionsDTO;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.service.component.redis.BudgetCache;
import com.zero.pennywise.service.component.redis.CategoryCache;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class TransactionHandler {

  private final BudgetCache budgetCache;
  private final CategoryCache categoryCache;
  private final TransactionRepository transactionRepository;

  // 잔액 업데이트 필요시 처리
  public void updateBalance(Long userId, TransactionDTO transactionDTO,
      String categoryName) {

    BalancesDTO balance = budgetCache.getBalances(userId, categoryName);
    if (balance == null) {
      return;
    }
    if ("지출".equals(transactionDTO.getType())) {
      balance.setBalance(balance.getBalance() - transactionDTO.getAmount());
    }

    budgetCache.updateBalance(userId, balance.getBalance(), categoryName);
  }

  // 거래 목록 유효값 검증
  public void validateTransactions(List<TransactionsDTO> transactions, String categoryName) {
    if (transactions == null || transactions.isEmpty()) {
      String message = StringUtils.hasText(categoryName)
          ? "존재하지 않은 카테고리 입니다."
          : "거래 내역에 존재하지 않습니다.";
      throw new GlobalException(HttpStatus.BAD_REQUEST, message);
    }
  }


  // 지난날 마지막 날짜 구하기
  public String getLastMonthsDate() {
    LocalDate lastMonthsDate = LocalDate.now().minusMonths(1);

    // 한 달 전의 마지막 날짜 계산
    YearMonth yearMonth = YearMonth.of(lastMonthsDate.getYear(), lastMonthsDate.getMonth());
    LocalDate lastDayOfLastMonth = yearMonth.atEndOfMonth();

    // 마지막 날에 맞춰 날짜를 조정
    if (lastMonthsDate.getDayOfMonth() > lastDayOfLastMonth.getDayOfMonth()) {
      lastMonthsDate = lastDayOfLastMonth;
    }
    return lastMonthsDate.toString();
  }


  // 거래 조회
  public TransactionEntity getTransaction(Long transactionId) {
    return transactionRepository.findByTransactionId(transactionId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 거래 아이디 입니다."));
  }

  // 입력한 데이터 수정
  public void updateTransactionDetails(TransactionEntity transaction, CategoriesEntity category,
      UpdateTransactionDTO updateTransaction) {

    transaction.setCategoryId(category.getCategoryId());
    transaction.setType(castToTransactionStatus(updateTransaction.getType(), updateTransaction.getIsFixed()));
    transaction.setAmount(updateTransaction.getAmount());
    transaction.setDescription(updateTransaction.getDescription());
  }


  // 캐시에 저장 되어 있는 예산 수정
  public void updateBalanceCacheData(Long userId, TransactionEntity transaction, UpdateTransactionDTO updateTransaction) {
    CategoriesEntity beforeCategory = categoryCache.getCategoryByCategoryId(userId, transaction.getCategoryId());
    boolean isSameCategory = beforeCategory.getCategoryName().equals(updateTransaction.getCategoryName());

    BalancesDTO balance = budgetCache.getBalances(userId, beforeCategory.getCategoryName());

    // 수정하기 전 거래가 지출일 경우 남은 예산 복구
    if (transaction.getType().isExpenses()) {
      balance.setBalance(balance.getBalance() + transaction.getAmount());
      budgetCache.updateBalance(userId, balance.getBalance(), balance.getCategoryName());
    }

    // 수정할 거래 카테고리가 같은 경우
    if (isSameCategory) {
      updateBeofreBalance(userId, balance, updateTransaction);
    } else {
      // 새로운 카테고리가 존재하는 경우, 새로운 카테고리의 잔액을 업데이트
      CategoriesEntity newCategory = categoryCache.getCategoryByCategoryName(userId,
          updateTransaction.getCategoryName());
      updateBalanceForNewCategory(userId, updateTransaction, newCategory);
    }
  }

  private void updateBeofreBalance(Long userId, BalancesDTO balance, UpdateTransactionDTO updateTransaction) {
    if ("지출".equals(updateTransaction.getType())) {
      balance.setBalance(balance.getBalance() - updateTransaction.getAmount());
      budgetCache.updateBalance(userId, balance.getBalance(), balance.getCategoryName());
    }
  }

  private void updateBalanceForNewCategory(Long userId, UpdateTransactionDTO updateTransaction, CategoriesEntity newCategory) {
    BalancesDTO balance = budgetCache.getBalances(userId, newCategory.getCategoryName());
    // 새로운 카테고리에서 지출이 발생한 경우 금액 차감
    if ("지출".equals(updateTransaction.getType())) {
      balance.setBalance(balance.getBalance() - updateTransaction.getAmount());
    }

    budgetCache.updateBalance(userId, balance.getBalance(), balance.getCategoryName());
  }

  // 거래 삭제시 남은 금액 변경
  public void deleteBalanceCacheData(Long userId, TransactionEntity transaction) {
    if (!transaction.getType().isExpenses()) {
      return;
    }
    CategoriesEntity beforeCategory = categoryCache
        .getCategoryByCategoryId(userId, transaction.getCategoryId());

    BalancesDTO beforeBalance = budgetCache.getBalances(userId, beforeCategory.getCategoryName());
    beforeBalance.setBalance(beforeBalance.getBalance() + transaction.getAmount());
    budgetCache.updateBalance(userId, beforeBalance.getBalance(), beforeBalance.getCategoryName());
  }

}
