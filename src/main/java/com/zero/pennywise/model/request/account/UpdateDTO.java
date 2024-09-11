package com.zero.pennywise.model.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDTO {

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;
  @NotBlank(message = "이름을 입력해주세요.")
  private String username;
  @NotBlank(message = "전화 번호를 입력해주세요.")
  private String phone;
}