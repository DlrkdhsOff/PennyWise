package com.zero.pennywise.domain.utils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatUtil {

  public static String formatWon(Long amount) {
    DecimalFormat koreanFormat = new DecimalFormat("#,###원");
    return koreanFormat.format(amount);
  }

  public static String formatOverBudgetMessage(String categoryName, String amount) {
    return "'" + categoryName + "'예산을 " + amount + " 초과했습니다.";

  }

  public static String formatTime(LocalDateTime localDateTime) {
    return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

}
