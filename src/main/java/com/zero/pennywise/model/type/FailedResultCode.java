package com.zero.pennywise.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FailedResultCode {

  EMAIL_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 가입된 아이디 입니다."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않은 회원 입니다."),
  PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치 하지 않습니다."),
  NICKNAME_ALREADY_USED(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임 입니다."),
  ;

  private final HttpStatus status;
  private final String message;
}
