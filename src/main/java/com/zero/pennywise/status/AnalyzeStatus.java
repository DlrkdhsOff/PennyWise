package com.zero.pennywise.status;

public enum AnalyzeStatus {

  BAD, GOOD, NOT_BAD;

  public String getMessage(String balance) {
    return switch (this) {
      case BAD -> "이번달 지출 금액이 지난 3달 평균금액보다 " + balance + "원을 더 지출 했어요";
      case NOT_BAD -> "이번달 지출 금액이 지난 3달 평균금액보다 " + balance + "%이상 사용했습니다.";
      case GOOD -> "이번달 지출 금액이 지난 3달 평균금액보다 " + balance + "원을 더 아꼈어요";
    };
  }

}
