package com.zero.pennywise.model.request.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDTO {

  @NotEmpty(message = "변경전 비밀번호를 입력해주세요.")
  private String beforePassword;

  @NotEmpty(message = "변경할 비밀번호를 입력해주세요.")
  private String afterPassword;

  @NotEmpty(message = "닉네임을 입력해주세요.")
  private String nickname;
}