package com.zero.pennywise.model.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateDTO {

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;
  @NotBlank(message = "이름을 입력해주세요.")
  private String username;
  @NotBlank(message = "전화 번호를 입력해주세요.")
  private String phone;
}