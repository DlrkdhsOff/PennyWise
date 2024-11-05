package com.zero.pennywise.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

  @NotEmpty(message = "아이디를 입력해주세요.")
  @Email(message = "이메일 형식이 아닙니다.")
  private String email;

  @NotEmpty(message = "비밀번호를 입력해주세요.")
  private String password;

  @NotEmpty(message = "닉네임을 입력해주세요.")
  private String nickname;

}
