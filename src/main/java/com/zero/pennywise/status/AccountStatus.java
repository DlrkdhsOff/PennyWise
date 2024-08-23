package com.zero.pennywise.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AccountStatus {
  // 회원가입
  REGISTER_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),
  REGISTER_FAILED_DUPLICATE_ID(HttpStatus.BAD_REQUEST, "중복된 아이디 입니다."),
  PHONE_NUMBER_CONTAINS_INVALID_CHARACTERS(HttpStatus.BAD_REQUEST, "전화번호에 유효하지 않은 문자가 포함되어 있습니다."),
  PHONE_NUMBER_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 전화번호 입니다."),

  // 로그인
  LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않은 아이디 입니다."),
  PASSWORD_DOES_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
  NOT_LOGGED_IN(HttpStatus.BAD_REQUEST, "로그인을 해주세요"),

  // 회원 정보 수정
  PARAMETER_IS_NULL(HttpStatus.BAD_REQUEST, "회원 정보 수정에 실패 했습니다."),
  UPDATE_SUCCESS(HttpStatus.OK, "회원 정보가 성공적으로 수정되었습니다."),

  // 회원 탈퇴
  ACCOUNT_DELETION_SUCCESS(HttpStatus.OK, "계정이 영구적으로 삭제 되었습니다."),
  ACCOUNT_DELETION_FAILED(HttpStatus.OK, "회원 탈퇴중 문제가 생겼습니다. 다시 시도 해주세요");

  private final HttpStatus status;
  private final String message;
}