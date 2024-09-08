package com.zero.pennywise.config;

import com.zero.pennywise.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerConfig {

  private final TransactionService transactionService;


  @Scheduled(cron = "0 0 0 * * ?")
  public void run() {

    // 고정 지출/수입 자동 등록
    transactionService.updateFixedTransaction();
  }
}
