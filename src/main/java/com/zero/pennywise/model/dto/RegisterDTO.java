package com.zero.pennywise.model.dto;

import com.zero.pennywise.model.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

  @NotBlank(message = "아이디를 입력해주세요.")
  private String userId;
  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;
  @NotBlank(message = "이름을 입력해주세요.")
  private String username;
  @NotBlank(message = "전화 번호를 입력해주세요.")
  private String phone;

  public static UserEntity of(RegisterDTO registerDTO) {
    return UserEntity.builder()
        .userId(registerDTO.getUserId())
        .password(registerDTO.getPassword())
        .username(registerDTO.getUsername())
        .phone(formatPhoneNumber(registerDTO.getPhone()))
        .createdAt(LocalDate.now())
        .build();
  }

  // 전화번호 formatting
  public static String formatPhoneNumber(String phoneNumber) {
    return phoneNumber.substring(0, 3) + "-" +
        phoneNumber.substring(3, 7) + "-" +
        phoneNumber.substring(7);
  }

}
