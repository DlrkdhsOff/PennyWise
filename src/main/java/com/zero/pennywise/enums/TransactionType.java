package com.zero.pennywise.enums;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.type.FailedResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {

  FIXED_EXPENSES("고정 지출"),
  FIXED_INCOME("고정 수입"),
  EXPENSES("지출"),
  INCOME("수입"),
  END("만기");

  private final String transactionType;

  public static TransactionType getTransactionType(String type) {
    return switch (type) {
      case "지출" -> EXPENSES;
      case "수입" -> INCOME;
      case "고정 지출" -> FIXED_EXPENSES;
      case "고정 수입" -> FIXED_INCOME;
      default -> throw new GlobalException(FailedResultCode.INVALID_TRANSACTION_TYPE);
    };
  }

  public static String of(TransactionType type) {
    return type.transactionType;
  }

  public boolean isExpenses() {
    return switch (this) {
      case EXPENSES, FIXED_EXPENSES -> true;
      case INCOME, FIXED_INCOME, END -> false;
    };
  }
}
