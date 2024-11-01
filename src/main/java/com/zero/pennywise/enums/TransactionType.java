package com.zero.pennywise.enums;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.type.FailedResultCode;

public enum TransactionType {

  FIXED_EXPENSES ,FIXED_INCOME, EXPENSES ,INCOME, END;

  public static TransactionType getEnumType(String type) {
    return switch (type) {
      case "지출" -> EXPENSES;
      case "수입" -> INCOME;
      case "고정 지출" -> FIXED_EXPENSES;
      case "고정 수입" -> FIXED_INCOME;
      default -> throw new GlobalException(FailedResultCode.INVALID_TRANSACTION_TYPE);
    };
  }

  public boolean isExpenses() {
    return switch (this) {
      case EXPENSES, FIXED_EXPENSES -> true;
      case INCOME, FIXED_INCOME, END -> false;
    };
  }
}
