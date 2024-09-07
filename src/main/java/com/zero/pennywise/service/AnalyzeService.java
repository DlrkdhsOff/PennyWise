package com.zero.pennywise.service;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.transaction.CategoryBalanceDTO;
import com.zero.pennywise.model.response.analyze.AnalyzeDTO;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import com.zero.pennywise.service.component.handler.AnalyzeHandler;
import com.zero.pennywise.service.component.handler.UserHandler;
import com.zero.pennywise.service.component.redis.CategoryCache;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyzeService {

  private final AnalyzeHandler analyzeHandler;
  private final UserHandler userHandler;



  public Object analyze(Long userId) {
    UserEntity user = userHandler.getUserById(userId);

    // 지난 3달 지출 금액
    AnalyzeDTO lastThreeMonth = analyzeHandler.getLastThreeMonthBalance(userId);

    // 이번달 지출 금액
    AnalyzeDTO thisMonth = analyzeHandler.getThisMonthBalance(userId);

    // 이번달 지출금액이 지난 3달간 평균 지출 금액보다 150% 이상일 경우
    if (lastThreeMonth.getTotalExpenses() * 2.5 < thisMonth.getTotalExpenses()) {
      // 임시
      analyzeHandler.sendMessage(user, "이번달 지출 금액이 지난 3달 평균금액보다 150%이상 사용했습니다.");
    }
    return null;
  }
}

