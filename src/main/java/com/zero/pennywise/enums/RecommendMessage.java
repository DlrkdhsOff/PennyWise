package com.zero.pennywise.enums;

public enum RecommendMessage {
  INCOME_TOO_LOW("수입 500,000원 이상부터 분석을 할 수 있습니다."),
  EXPENSES_TOO_HIGH("평균 지출이 수입의 80%% 이상입니다. 지출을 줄이고 저축을 해보세요."),
  RECOMMENDED_SAVINGS("추천 저축 금액은 수입의 20%%인 %,d원 입니다."),
  ONE_YEAR_SAVINGS("1년 동안 %,d원을 저축하면 %,d원 모을 수 있습니다."),
  GOOD_PROGRESS("저축 습관을 가지고 계신 것은 좋지만 %,d원을 더 저축해보세요."),
  KEEP_GOING("아주 잘 하고 있습니다. 지금처럼 유지하면 1년 동안 %,d원을 모을 수 있습니다.");

  private final String message;

  RecommendMessage(String message) {
    this.message = message;
  }

  public String getMessage(Object... params) {
    return String.format(message, params);
  }

  public static String evaluate(Long totalIncome, Long totalExpenses, Long savingAccount) {
    StringBuilder sb = new StringBuilder();

    // 1. 수입이 너무 적을 경우
    if (totalIncome < 500000L) {
      return INCOME_TOO_LOW.getMessage();
    }

    // 추천 저축 금액 계산 (수입의 20%)
    long recommendedSavings = (long)(totalIncome * 0.2);

    // 2. 저축을 하지 않은 경우
    if (savingAccount == null || savingAccount == 0L) {
      // 지출이 수입의 80% 이상인 경우
      if (recommendedSavings > totalIncome - totalExpenses) {
        sb.append(EXPENSES_TOO_HIGH.getMessage()).append("\n");
      }
      sb.append(RECOMMENDED_SAVINGS.getMessage(recommendedSavings)).append("\n")
          .append(ONE_YEAR_SAVINGS.getMessage(recommendedSavings, recommendedSavings * 12));

      // 3. 저축을 하고 있는 경우
    } else {
      // 저축이 추천 금액에 미치지 못하는 경우
      if (savingAccount < recommendedSavings) {
        sb.append(GOOD_PROGRESS.getMessage(recommendedSavings - savingAccount)).append("\n")
            .append(ONE_YEAR_SAVINGS.getMessage(recommendedSavings, recommendedSavings * 12));
      }
      // 저축이 추천 금액 이상인 경우
      else {
        sb.append(KEEP_GOING.getMessage(recommendedSavings * 12)).append("\n");
      }
    }

    return sb.toString();
  }
}