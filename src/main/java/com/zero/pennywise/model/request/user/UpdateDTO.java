package com.zero.pennywise.model.request.user;

import com.zero.pennywise.entity.UserEntity;
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


  public static UserEntity of(UserEntity user, UpdateDTO updateDTO, String afterPassword) {
    return UserEntity.builder()
        .userId(user.getUserId())
        .email(user.getEmail())
        .password(afterPassword)
        .nickname(updateDTO.getNickname())
        .role(user.getRole())
        .build();
  }
}