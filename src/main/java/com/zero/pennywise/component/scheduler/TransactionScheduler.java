package com.zero.pennywise.component.scheduler;

import com.zero.pennywise.component.handler.CategoryHandler;
import com.zero.pennywise.component.handler.SavingHandler;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import com.zero.pennywise.status.TransactionStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionScheduler {

  private final TransactionQueryRepository transactionQueryRepository;
  private final TransactionRepository transactionRepository;
  private final SavingHandler savingHandler;


  // 지정한 기간에 새로 등록
  public void schedulePayment(UserEntity user, SavingsEntity savings) {
    LocalDateTime startDate = savings.getStartDate().atStartOfDay();
    Instant paymentTime = startDate.atZone(ZoneId.systemDefault()).toInstant();

    TaskScheduler taskScheduler = setTreadPool();
    taskScheduler.schedule(() -> savingHandler.payment(user, savings), paymentTime);
  }


  public void scheduleEnd(UserEntity user, SavingsEntity savings) {
    LocalDateTime endDate = savings.getEndDate().atStartOfDay();
    Instant paymentTime = endDate.atZone(ZoneId.systemDefault()).toInstant();

    TaskScheduler taskScheduler = setTreadPool();
    taskScheduler.schedule(() -> savingHandler.endDeposit(user, savings), paymentTime);
  }


  public TaskScheduler setTreadPool() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(10);
    scheduler.initialize();
    return scheduler;
  }


  // 매일 00시 고정 수입/지출 자동 등록
  public void updateFixedTransaction() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String lastMonthsDate = LocalDate.now().minusMonths(1).toString();  // 한 달 전 날짜

    List<TransactionEntity> transactions = transactionQueryRepository
        .findByLastMonthTransaction(lastMonthsDate);

    updateFixedTransactionDetail(transactions);
  }

  public void updateFixedTransactionDetail(List<TransactionEntity> transactions) {
    transactions.forEach(transaction -> transactionRepository.save(
        TransactionEntity.builder()
            .user(transaction.getUser())
            .categoryId(transaction.getCategoryId())
            .type(transaction.getType())
            .amount(transaction.getAmount())
            .description(transaction.getDescription())
            .dateTime(LocalDateTime.now())
            .build()));
  }

}
