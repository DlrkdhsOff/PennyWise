package com.zero.pennywise.service;

import com.zero.pennywise.model.dto.LoginDTO;
import com.zero.pennywise.model.dto.RegisterDTO;
import com.zero.pennywise.model.dto.UpdateDTO;
import com.zero.pennywise.model.entity.UserEntity;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.status.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  // 회원 가입
  public Response register(RegisterDTO registerDTO) {

    // 중복 아이디 체크
    if (userRepository.existsByEmail(registerDTO.getEmail())) {
      return new Response(AccountStatus.REGISTER_FAILED_DUPLICATE_ID);
    }

    Response response = validatePhoneNumber(registerDTO.getPhone());

    if (response != null) {
      return response;
    } else {
      registerDTO.setPhone(registerDTO.getPhone());
    }

    UserEntity userEntity = RegisterDTO.of(registerDTO);
    userRepository.save(userEntity);

    return new Response(AccountStatus.REGISTER_SUCCESS);
  }

  // 로그인
  public Response login(LoginDTO loginDTO) {

    // 아이디 존재여부 확인
    if (!userRepository.existsByEmail(loginDTO.getEmail())) {
      return new Response(AccountStatus.USER_NOT_FOUND);
    }

    UserEntity user = userRepository.findByEmail(loginDTO.getEmail());

    // 비밀번호 일치 확인
    if (!user.getPassword().equals(loginDTO.getPassword())) {
      return new Response(AccountStatus.PASSWORD_DOES_NOT_MATCH);
    }

    return new Response(AccountStatus.LOGIN_SUCCESS);
  }

  // 회원 정보 수정
  public Response update(String email, UpdateDTO updateDTO) {
    // 회원 정보 조회
    UserEntity user = userRepository.findByEmail(email);

    if (updateDTO == null || updateDTO.getPassword().isBlank() && updateDTO.getUsername().isBlank()
        && updateDTO.getPhone().isBlank()) {
      return new Response(AccountStatus.PARAMETER_IS_NULL);
    }

    // 비밀번호 업데이트
    if (updateDTO.getPassword() != null && !updateDTO.getPassword().isBlank()) {
      user.setPassword(updateDTO.getPassword());
    }

    // 사용자 이름 업데이트
    if (updateDTO.getUsername() != null && !updateDTO.getUsername().isBlank()) {
      user.setUsername(updateDTO.getUsername());
    }

    // 전화번호 업데이트
    if (updateDTO.getPhone() != null && !updateDTO.getPhone().isBlank()) {
      Response validationResponse = validatePhoneNumber(updateDTO.getPhone());
      if (validationResponse != null) {
        return validationResponse;
      }
      user.setPhone(updateDTO.getPhone());
    }

    // 업데이트 완료 응답 반환
    return new Response(AccountStatus.UPDATE_SUCCESS);
  }


  public Response delete(String email) {

    if (!userRepository.existsByEmail(email)) {
      userRepository.deleteByEmail(email);
      return new Response(AccountStatus.ACCOUNT_DELETION_SUCCESS);
    }

    return new Response(AccountStatus.ACCOUNT_DELETION_FAILED);
  }

  // 전화 번호 유효성 확인
  public Response validatePhoneNumber(String phone) {
    if (!phone.matches("^[0-9]+$")) {
      return new Response(AccountStatus.PHONE_NUMBER_CONTAINS_INVALID_CHARACTERS);
    } else if (phone.length() > 11) {
      return new Response(AccountStatus.PHONE_NUMBER_INVALID);
    }

    return null;
  }

  // 전화번호 formatting
  public String formatPhoneNumber(String phoneNumber) {
    return phoneNumber.substring(0, 3) + "-" +
        phoneNumber.substring(3, 7) + "-" +
        phoneNumber.substring(7);
  }
}
