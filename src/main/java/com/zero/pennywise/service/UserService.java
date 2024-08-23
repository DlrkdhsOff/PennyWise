package com.zero.pennywise.service;

import com.zero.pennywise.model.dto.LoginDTO;
import com.zero.pennywise.model.dto.RegisterDTO;
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

    // 전화번호에 유효하지 않은 문자 유무 확인
    if (!registerDTO.getPhone().matches("^[0-9]+$")) {
      return new Response(AccountStatus.PHONE_NUMBER_CONTAINS_INVALID_CHARACTERS);

    // 전화번호 길이 확인
    } else if (registerDTO.getPhone().length() > 11) {
      return new Response(AccountStatus.PHONE_NUMBER_INVALID);
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
}
