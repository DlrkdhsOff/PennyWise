package com.zero.pennywise.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FailedResultCode {

  // User
  EMAIL_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 가입된 아이디 입니다."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않은 회원 입니다."),
  PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치 하지 않습니다."),
  NICKNAME_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임 입니다."),

  // Category
  CATEGORY_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 등록한 카테고리 입니다."),
  CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."),

  // Budget
  BUDGET_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 등록한 예산입니다."),
  BUDGET_NOT_FOUND(HttpStatus.BAD_REQUEST, "예산을 찾을 수 없습니다."),

  // Transaction
  INVALID_TRANSACTION_TYPE(HttpStatus.BAD_REQUEST, "거래 타입을 정확하게 입력해주세요."),
  TRANSACTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "거래 내역이 존재하지 않습니다."),

  // Balance
  AVERAGE_INCOME_TOO_LOW(HttpStatus.BAD_REQUEST, "평균 수입 금액이 너무 적습니다.");

  ;

  private final HttpStatus status;
  private final String message;
}
