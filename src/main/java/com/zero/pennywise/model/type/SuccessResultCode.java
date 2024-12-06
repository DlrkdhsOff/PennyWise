package com.zero.pennywise.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessResultCode {

  // User
  SUCCESS_SIGNUP(HttpStatus.CREATED, "회원가입 성공!"),
  SUCCESS_LOGIN(HttpStatus.OK, "로그인 성공!"),
  SUCCESS_GET_USER_INFO(HttpStatus.OK, "회원 정보 조회 성공!"),
  SUCCESS_UPDATE_USER_INFO(HttpStatus.OK, "회원 정보가 성공적으로 수정되었습니다!"),
  SUCCESS_DELETE_USER_INFO(HttpStatus.OK, "계정이 영구적으로 삭제 되었습니다!"),

  // Category
  SUCCESS_GET_CATEGORY_LIST(HttpStatus.OK, "카테고리 목록 조회 성공!"),
  SUCCESS_CREATE_CATEGORY(HttpStatus.CREATED, "카테고리 등록 성공!"),
  SUCCESS_UPDATE_CATEGORY(HttpStatus.OK, "카테고리 수정 성공!"),
  SUCCESS_DELETE_CATEGORY(HttpStatus.OK, "카테고리 삭제 성공!"),

  // Budget
  SUCCESS_GET_BUDGET_LIST(HttpStatus.OK, "예산 목록 조회 성공!"),
  SUCCESS_CREATE_BUDGET(HttpStatus.CREATED, "예산 등록 성공!"),
  SUCCESS_UPDATE_BUDGET(HttpStatus.OK, "예산 수정 성공!"),
  SUCCESS_DELETE_BUDGET(HttpStatus.OK, "예산 삭제 성공!"),

  // Transaction
  SUCCESS_CREATE_TRANSACTION(HttpStatus.CREATED, "거래 등록 성공!"),
  SUCCESS_GET_TRANSACTION_INFO(HttpStatus.OK, "거래 목록 조회 성공!"),
  SUCCESS_UPDATE_TRANSACTION(HttpStatus.OK, "거래 수정 성공!"),
  SUCCESS_DELETE_TRANSACTION(HttpStatus.OK, "거래 삭제 성공!"),
  ;

  private final HttpStatus status;
  private final String message;
}
