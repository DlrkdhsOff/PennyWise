package com.zero.pennywise.component.scheduler;

import com.zero.pennywise.component.handler.SavingHandler;
import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TransactionScheduler {

  private final TransactionQueryRepository transactionQueryRepository;
  private final TransactionRepository transactionRepository;
  private final SavingHandler savingHandler;
  private final TaskScheduler multiThreadScheduler;
  private final TaskScheduler singleThreadScheduler;


  // 지정한 기간에 새로 등록
  public void schedulePayment(UserEntity user, SavingsEntity savings) {
    LocalDateTime startDate = savings.getStartDate().atStartOfDay().withHour(3).withMinute(55).withSecond(0);
    Instant paymentTime = startDate.atZone(ZoneId.systemDefault()).toInstant();

    multiThreadScheduler.schedule(() -> savingHandler.payment(user, savings), paymentTime);
  }


  public void scheduleEnd(UserEntity user, SavingsEntity savings) {
    LocalDateTime endDate = savings.getEndDate().atStartOfDay();
    Instant paymentTime = endDate.atZone(ZoneId.systemDefault()).toInstant();

    multiThreadScheduler.schedule(() -> savingHandler.endDeposit(user, savings), paymentTime);
  }


  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional
  public void run() {
    String lastMonthsDate = LocalDate.now().minusMonths(1).toString();

    List<TransactionEntity> transactions = transactionQueryRepository.findByLastMonthTransaction(lastMonthsDate);

    singleThreadScheduler.schedule(() -> updateFixedTransactionDetail(transactions), Instant.now());
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
