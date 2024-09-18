package com.zero.pennywise.enums;

import com.zero.pennywise.exception.GlobalException;
import org.springframework.http.HttpStatus;

public enum TransactionStatus {

  FIXED_EXPENSES ,FIXED_INCOME, EXPENSES ,INCOME, END;

  public static TransactionStatus getEnumType(String type) {
    return switch (type) {
      case "지출" -> EXPENSES;
      case "수입" -> INCOME;
      case "고정 지출" -> FIXED_EXPENSES;
      case "고정 수입" -> FIXED_INCOME;
      default -> throw new GlobalException(HttpStatus.BAD_REQUEST, "수입/지출 타입을 정확하게 입력해주세요");
    };
  }

  public boolean isExpenses() {
    return switch (this) {
      case EXPENSES, FIXED_EXPENSES -> true;
      case INCOME, FIXED_INCOME, END -> false;
    };
  }
}
