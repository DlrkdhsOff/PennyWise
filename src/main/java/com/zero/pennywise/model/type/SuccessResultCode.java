package com.zero.pennywise.model.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessResultCode {
  SUCCESS_SIGNUP(HttpStatus.CREATED, "회원가입 성공!"),
  SUCCESS_LOGIN(HttpStatus.OK, "로그인 성공!"),
  SUCCESS_GET_USER_INFO(HttpStatus.OK, "회원 정보 조회 성공!"),
  SUCCESS_UPDATE_USER_INFO(HttpStatus.OK, "회원 정보가 성공적으로 수정되었습니다!"),
  SUCCESS_DELETE_USER_INFO(HttpStatus.OK, "계정이 영구적으로 삭제 되었습니다!")
  ;

  private final HttpStatus status;
  private final String message;
}
