package com.zero.pennywise.enums;

import com.zero.pennywise.model.request.transaction.CategoryBalanceDTO;
import com.zero.pennywise.model.response.analyze.AnalyzeDTO;

public enum AnalyzeMessage {
  LAST_THREE_MONTH_AVG("지난 3달간 평균 지출 총액 : %,d원\n"),
  THIS_MONTH_TOTAL("\n현재 평균 지출 총액 : %,d원\n"),
  CATEGORY_EXPENSE("카테고리 %s : %,d원\n"),

  EXPENSES_TOO_HIGH("지난 3달간 평균 지출 금액보다 %,d원 더 사용 했습니다.\n"),
  MAX_EXPENSES_CATEGORY("지출이 가장 많은 카테고리 : %s\n 사용 금액 %,d원\n");

  private final String message;

  AnalyzeMessage(String message) {
    this.message = message;
  }

  public String generateMessage(Object... params) {
    return String.format(message, params);
  }

  public static String getMessage(Long lastTotalAmount, Long thisMonthAmount, AnalyzeDTO lastThreeMonth, AnalyzeDTO thisMonth) {
    StringBuilder sb = new StringBuilder();

    // 지난 3달 평균 지출
    sb.append(LAST_THREE_MONTH_AVG.generateMessage(lastTotalAmount));
    for (CategoryBalanceDTO dto : lastThreeMonth.getCategoryBalances()) {
      sb.append(CATEGORY_EXPENSE.generateMessage(dto.getCategoryName(), dto.getTotalExpenses()));
    }

    // 이번달 지출
    sb.append(THIS_MONTH_TOTAL.generateMessage(thisMonthAmount));
    for (CategoryBalanceDTO dto : thisMonth.getCategoryBalances()) {
      sb.append(CATEGORY_EXPENSE.generateMessage(dto.getCategoryName(), dto.getTotalExpenses()));
    }

    return sb.toString();
  }

}