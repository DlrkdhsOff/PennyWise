package com.zero.pennywise.model.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

  @NotBlank(message = "아이디를 입력해주세요.")
  private String email;
  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;
}