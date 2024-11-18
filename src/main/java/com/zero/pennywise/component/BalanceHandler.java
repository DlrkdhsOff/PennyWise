package com.zero.pennywise.component;

import com.zero.pennywise.entity.BalanceEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.repository.BalanceRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceHandler {

  private final BalanceRepository balanceRepository;
  private final RedisHandler redisHandler;

  // 거래 생성 또는 추가
  public void addBalance(UserEntity user, TransactionEntity transaction) {
    String recordMonth = getRecordMonth();
    BalanceEntity balance = getOrCreateBalance(user, transaction.getCategory(), recordMonth);
    addTransactionAmount(balance, transaction.getAmount(), transaction.getType().isExpenses());
    saveBalance(balance);
    redisHandler.updateBalance(user, balance);
  }

  // 거래 갱신 또는 수정
  public void updateBalance(UserEntity user, TransactionEntity transaction, TransactionEntity newTransaction) {
    String recordMonth = getRecordMonth();

    // 같은 카테고리인 경우
    if (transaction.getCategory().equals(newTransaction.getCategory())) {
      BalanceEntity balance = getOrCreateBalance(user, transaction.getCategory(), recordMonth);
      subtractTransactionAmount(balance, transaction.getAmount(), transaction.getType().isExpenses());
      addTransactionAmount(balance, newTransaction.getAmount(), newTransaction.getType().isExpenses());
      saveBalance(balance);
      redisHandler.updateBalance(user, balance);
    } else {
      // 기존 거래에서 금액 제거
      BalanceEntity oldBalance = getOrCreateBalance(user, transaction.getCategory(), recordMonth);
      subtractTransactionAmount(oldBalance, transaction.getAmount(), transaction.getType().isExpenses());
      saveBalance(oldBalance);
      redisHandler.updateBalance(user, oldBalance);

      // 새 거래에 금액 추가
      BalanceEntity newBalance = getOrCreateBalance(user, newTransaction.getCategory(), recordMonth);
      addTransactionAmount(newBalance, newTransaction.getAmount(), newTransaction.getType().isExpenses());
      saveBalance(newBalance);
      redisHandler.updateBalance(user, newBalance);
    }
  }

  // 거래 삭제
  public void deleteBalance(UserEntity user, TransactionEntity transaction) {
    String recordMonth = getRecordMonth();

    // 기존 거래에서 금액 제거
    BalanceEntity balance = getOrCreateBalance(user, transaction.getCategory(), recordMonth);
    subtractTransactionAmount(balance, transaction.getAmount(), transaction.getType().isExpenses());
    saveBalance(balance);
    redisHandler.updateBalance(user, balance);
  }

  // 현재 연도와 월을 문자열로 가져오는 메서드
  private String getRecordMonth() {
    return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
  }

  // BalanceEntity를 가져오거나 생성하는 메서드
  private BalanceEntity getOrCreateBalance(UserEntity user, CategoryEntity category, String recordMonth) {
    return balanceRepository.findByUserAndCategoryAndRecordMonth(user, category, recordMonth)
        .orElseGet(() -> BalanceEntity.builder()
            .user(user)
            .category(category)
            .recordMonth(recordMonth)
            .totalIncomeAmount(0L)
            .totalExpensesAmount(0L)
            .build());
  }

  // 수입 또는 지출을 추가하는 메서드
  private void addTransactionAmount(BalanceEntity balance, Long amount, boolean isExpense) {
    if (isExpense) {
      balance.addExpensesAmount(amount);
    } else {
      balance.addIncomeAmount(amount);
    }
  }

  // 수입 또는 지출을 빼는 메서드
  private void subtractTransactionAmount(BalanceEntity balance, Long amount, boolean isExpense) {
    if (isExpense) {
      balance.subtractExpensesAmount(amount);
    } else {
      balance.subtractIncomeAmount(amount);
    }
  }

  // 수입 / 지출 정보 저장
  private void saveBalance(BalanceEntity balance) {
    balanceRepository.save(balance);
  }
}