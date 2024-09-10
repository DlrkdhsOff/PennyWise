package com.zero.pennywise.component.scheduler;

import com.zero.pennywise.component.handler.SavingHandler;
import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.UserEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionScheduler {

  private final SavingHandler savingHandler;
  private final TaskScheduler taskScheduler;
  private final Logger logger = LoggerFactory.getLogger(BatchScheduler.class);


  // 지정한 기간에 새로 등록
  public void schedulePayment(UserEntity user, SavingsEntity savings) {
    try {
      LocalDateTime startDate = savings.getStartDate().atStartOfDay().withHour(9).withMinute(0).withSecond(0);
      Instant paymentTime = startDate.atZone(ZoneId.systemDefault()).toInstant();
      taskScheduler.schedule(() -> savingHandler.payment(user, savings), paymentTime);
    } catch (Exception e) {
      logger.error("Error scheduling payment for user: {}, savings: {}", user.getId(), savings.getId(), e);
    }
  }

  // 만기
  public void scheduleEnd(UserEntity user, SavingsEntity savings) {
    try {
      LocalDateTime endDate = savings.getEndDate().atStartOfDay();
      Instant paymentTime = endDate.atZone(ZoneId.systemDefault()).toInstant();

      taskScheduler.schedule(() -> savingHandler.endDeposit(user, savings), paymentTime);
    } catch (Exception e) {
      logger.error("Error scheduling payment for user: {}, savings: {}", user.getId(), savings.getId(), e);
    }
  }
}
