package com.zero.pennywise.service;

import com.zero.pennywise.component.handler.AnalyzeHandler;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.analyze.AnalyzeDTO;
import com.zero.pennywise.status.AnalyzeStatus;
import java.text.NumberFormat;
import java.util.Locale;
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
      Long lastTotalAmount = lastThreeMonth.getTotalExpenses();
      Long thisMonthAmount = thisMonth.getTotalExpenses();

      NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
      String balance = numberFormat.format(thisMonthAmount - lastTotalAmount);
      analyzeHandler.sendMessage(user, AnalyzeStatus.BAD.getMessage(balance));
    }
    return null;
  }
}

