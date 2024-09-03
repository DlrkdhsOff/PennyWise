package com.zero.pennywise.model.dto.account;

import com.zero.pennywise.model.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

  @NotBlank(message = "아이디를 입력해주세요.")
  private String email;
  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;
  @NotBlank(message = "이름을 입력해주세요.")
  private String username;
  @NotBlank(message = "전화 번호를 입력해주세요.")
  private String phone;

  public static UserEntity of(RegisterDTO registerDTO) {
    return UserEntity.builder()
        .email(registerDTO.getEmail())
        .password(registerDTO.getPassword())
        .username(registerDTO.getUsername())
        .phone(registerDTO.getPhone())
        .build();
  }

}
