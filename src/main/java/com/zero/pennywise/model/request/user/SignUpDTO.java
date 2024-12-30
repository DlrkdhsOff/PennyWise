package com.zero.pennywise.model.request.user;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.type.UserRole;
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
public class SignUpDTO {

  @NotEmpty(message = "아이디를 입력해주세요.")
  @Email(message = "이메일 형식이 아닙니다.")
  private String email;

  @NotEmpty(message = "비밀번호를 입력해주세요.")
  private String password;

  @NotEmpty(message = "닉네임을 입력해주세요.")
  private String nickname;

  public static UserEntity of (SignUpDTO signUpDTO, String password) {
    return UserEntity.builder()
        .email(signUpDTO.getEmail())
        .password(password)
        .nickname(signUpDTO.getNickname())
        .role(UserRole.USER)
        .build();
  }

}
