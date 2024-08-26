package com.zero.pennywise.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BudgetTrackerStatus {

  SUCCESS_CREATE_CATEGORY(HttpStatus.CREATED, "카테고리를 생성하였습니다."),
  CATEGORY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다."),
  CATEGORY_IS_NULL(HttpStatus.BAD_REQUEST, "카테고리를 입력해주세요."),
  CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다."),

  SUCCESS_SET_BUDGET(HttpStatus.OK, "성공적으로 예산을 설정 했습니다."),

  SUCCESS_TRANSACTION_REGISTRATION(HttpStatus.OK, "거래 등록 성공"),
  TRANSACTIONS_NOT_FOUND(HttpStatus.NOT_FOUND, "거래내역이 존재 하지 않습니다.");

  private final HttpStatus status;
  private final String message;
}