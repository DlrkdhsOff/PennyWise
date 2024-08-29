package com.zero.pennywise.service;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.LoginDTO;
import com.zero.pennywise.model.dto.RegisterDTO;
import com.zero.pennywise.model.dto.UpdateDTO;
import com.zero.pennywise.model.entity.UserEntity;
import com.zero.pennywise.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  // 회원 가입
  public String register(RegisterDTO registerDTO) {

    // 중복 아이디 체크
    if (userRepository.existsByEmail(registerDTO.getEmail())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디 입니다.");
    }

    validatePhoneNumber(registerDTO.getPhone());
    registerDTO.setPhone(registerDTO.getPhone());

    userRepository.save(RegisterDTO.of(registerDTO));

    return "회원가입 성공";
  }

  // 로그인
  public String login(LoginDTO loginDTO, HttpServletRequest request) {

    // 아이디 존재여부 확인
    if (!userRepository.existsByEmail(loginDTO.getEmail())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 아이디 입니다.");
    }

    UserEntity user = userRepository.findByEmail(loginDTO.getEmail());

    // 비밀번호 일치 확인
    if (!user.getPassword().equals(loginDTO.getPassword())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
    }

    request.getSession().setAttribute("userId", user.getId());
    return "로그인 성공";
  }


  // 회원 정보 수정
  public String update(Long userId, UpdateDTO updateDTO) {
    Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);

    if (optionalUserEntity.isEmpty()) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 아이디 입니다.");
    }

    UserEntity user = optionalUserEntity.get();
    userRepository.save(validateUpdateDTO(user, updateDTO));

    return "회원 정보가 성공적으로 수정되었습니다.";
  }

  // 회원 탈퇴
  public String delete(Long userId) {

    if (!userRepository.existsById(userId)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "회원 탈퇴 실패하였습니다.");
    }

    userRepository.deleteById(userId);
    return "계정이 영구적으로 삭제 되었습니다.";
  }

  // 전화 번호 유효성 확인
  public void validatePhoneNumber(String phone) {
    if (!phone.matches("^[0-9]+$")) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "전화번호에 유효하지 않은 문자가 포함되어 있습니다.");
    } else if (phone.length() > 11) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "유효하지 않은 전화번호 입니다.");
    }
  }

  public UserEntity validateUpdateDTO(UserEntity user, UpdateDTO updateDTO) {
    if (!StringUtils.hasText(updateDTO.getPassword())
        && !StringUtils.hasText(updateDTO.getUsername())
        && !StringUtils.hasText(updateDTO.getPhone())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "회원 정보 수정에 실패 했습니다.");
    }

    // 비밀번호 업데이트
    if (StringUtils.hasText(updateDTO.getPassword())) {
      user.setPassword(updateDTO.getPassword());
    }

    // 사용자 이름 업데이트
    if (StringUtils.hasText(updateDTO.getUsername())) {
      user.setUsername(updateDTO.getUsername());
    }

    // 전화번호 업데이트
    if (StringUtils.hasText(updateDTO.getPhone())) {
      validatePhoneNumber(updateDTO.getPhone());
      user.setPhone(formatPhoneNumber(updateDTO.getPhone()));
    }

    return user;
  }

  // 전화번호 formatting
  public String formatPhoneNumber(String phoneNumber) {
    return phoneNumber.substring(0, 3) + "-" +
        phoneNumber.substring(3, 7) + "-" +
        phoneNumber.substring(7);
  }

}
