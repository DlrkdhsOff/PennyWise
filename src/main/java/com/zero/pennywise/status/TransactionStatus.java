package com.zero.pennywise.status;

public enum TransactionStatus {

  FIXED_EXPENSES ,FIXED_INCOME, EXPENSES ,INCOME;

  public static TransactionStatus castToTransactionStatus(String type, String isFixed) {
    if ("지출".equals(type) && "Y".equals(isFixed)) {
      return TransactionStatus.FIXED_EXPENSES;
    } else if ("지출".equals(type)) {
      return TransactionStatus.EXPENSES;
    } else if ("Y".equals(isFixed)) {
      return TransactionStatus.FIXED_INCOME;
    } else {
      return TransactionStatus.INCOME;
    }
  }

  public Long calculate(Long currentValue, Long amount) {
    return switch (this) {
      case EXPENSES, FIXED_EXPENSES -> currentValue - amount;
      case INCOME, FIXED_INCOME -> currentValue + amount;
    };
  }
}
