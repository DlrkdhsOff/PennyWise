package com.zero.pennywise.status;

import com.zero.pennywise.exception.GlobalException;
import javax.swing.plaf.BorderUIResource.EtchedBorderUIResource;
import org.springframework.http.HttpStatus;

public enum TransactionStatus {

  FIXED_EXPENSES ,FIXED_INCOME, EXPENSES ,INCOME;

  public static TransactionStatus castToTransactionStatus(String type, String isFixed) {
    return switch (type) {
      case "지출" -> isFixed.equals("Y") ? FIXED_EXPENSES : EXPENSES;
      case "수입" -> isFixed.equals("Y") ? FIXED_INCOME : INCOME;
      default -> throw new GlobalException(HttpStatus.BAD_REQUEST, "수입/지출 타입을 정확하게 입력해주세요");
    };
  }

  public boolean isExpenses() {
    return switch (this) {
      case EXPENSES, FIXED_EXPENSES -> true;
      case INCOME, FIXED_INCOME -> false;
    };
  }

  public boolean isExpensesStr(String type) {
    return switch (type) {
      case "지출" -> true;
      case "수입" -> false;
      default -> throw new GlobalException(HttpStatus.BAD_REQUEST, "수입/지출 타입을 정확하게 입력해주세요");
    };
  }
}
