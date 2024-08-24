package com.zero.pennywise.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BudgetTrackerStatus {

  SUCCESS_CREATE_CATEGORY(HttpStatus.CREATED, "카테고리를 생성하였습니다."),
  CATEGORY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리 입니다."),

  SUCCESS_SET_BUDGET(HttpStatus.OK, "성공적으로 예산을 설정 했습니다.");

  private final HttpStatus status;
  private final String message;
}