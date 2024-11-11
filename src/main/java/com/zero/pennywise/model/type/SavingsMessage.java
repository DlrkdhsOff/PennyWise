package com.zero.pennywise.model.type;

import java.text.NumberFormat;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SavingsMessage {
  RECOMMENDED_SAVINGS("추천 저축 금액은 수입의 20%%인 %s원 입니다."),
  HIGH_SPENDING_WARNING("최근 3개월 지출 중 %s의 지출이 많습니다. 해당 카테고리의 지출을 줄이고, 수입의 20%%인 %s원을 저축해보세요!");
  ;

  private final String message;

  public String getMessage(long amount) {
    String result = NumberFormat.getInstance(Locale.KOREA).format(amount);
    return String.format(message, result);
  }

  public String getMessage(long amount, String categoryName) {
    String result = NumberFormat.getInstance(Locale.KOREA).format(amount);
    return String.format(message, categoryName, result);
  }
}
