package com.zero.pennywise.core.component;


import com.zero.pennywise.core.auth.jwt.JwtUtil;
import com.zero.pennywise.domain.entity.UserEntity;
import com.zero.pennywise.core.exception.GlobalException;
import com.zero.pennywise.domain.model.request.user.LoginDTO;
import com.zero.pennywise.domain.model.request.user.SignUpDTO;
import com.zero.pennywise.domain.model.type.FailedResultCode;
import com.zero.pennywise.domain.model.type.TokenType;
import com.zero.pennywise.domain.model.type.UserRole;
import com.zero.pennywise.domain.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 파사드 컴포넌트
 * 서비스와 리포지토리 계층 사이의 중간 계층으로써 복잡한 사용자 관련 로직을 캡슐화
 */
@Component
@RequiredArgsConstructor
public class UserFacade {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final BCryptPasswordEncoder passwordEncoder;

  /**
   * 이메일 중복 검증
   *
   * @param email 검증할 이메일
   * @return 검증 결과 메시지
   */
  public String validateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      return "이미 사용중인 이메일 입니다.";
    }
    return "사용할 수 있는 이메일 입니다.";
  }

  /**
   * 닉네임 중복 검증
   *
   * @param nickname 검증할 닉네임
   * @return 검증 결과 메시지
   */
  public String validateNickname(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      return "이미 사용중인 닉네임 입니다.";
    }
    return "사용할 수 있는 닉네임 입니다.";
  }

  /**
   * 비밀번호 일치 여부 검증
   *
   * @param user 검증 대상 사용자 엔티티
   * @param password 검증할 비밀번호
   * @return 검증 결과 메시지
   */
  public String validatePassword(UserEntity user, String password) {
    if (passwordEncoder.matches(password, user.getPassword())) {
      return "비밀번호가 일치 합니다.";
    }
    return "비밀번호가 일치하지 않습니다.";
  }

  /**
   * 회원가입 처리: 새 사용자 생성 및 저장
   *
   * @param signUpDTO 회원가입 정보
   */
  public void createAndSaveUser(SignUpDTO signUpDTO) {
    UserEntity user = SignUpDTO.of(signUpDTO, passwordEncoder.encode(signUpDTO.getPassword()));
    userRepository.save(user);
  }

  /**
   * 로그인 시 입력한 사용자 정보 검증
   * 이메일과 일치하는 UserEntity 조회 및 비밀번호 일치 여부 검증
   *
   * @param loginDTO 로그인시 입력한 사용자 정보
   * @return 검증된 사용자 엔티티
   * @throws GlobalException 사용자가 없거나 비밀번호가 일치하지 않을 경우
   */
  public UserEntity validateLoginData(LoginDTO loginDTO) {
    UserEntity user = userRepository.findByEmail(loginDTO.getEmail())
        .orElseThrow(() -> new GlobalException(FailedResultCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
      throw new GlobalException(FailedResultCode.PASSWORD_MISMATCH);
    }

    return user;
  }

  /**
   * JWT 토큰 생성 및 응답에 추가
   *
   * @param response HTTP 응답 객체
   * @param user 토큰 생성 대상 사용자
   */
  public void generateToken(HttpServletResponse response, UserEntity user) {
    String access = jwtUtil.createJwt("access", user.getUserId(), UserRole.USER);
    String refresh = jwtUtil.createJwt("refresh", user.getUserId(), UserRole.USER);

    response.addHeader(TokenType.ACCESS.getValue(), "Bearer " + access);
    response.addCookie(jwtUtil.setCookie(refresh));
  }

  /**
   * 액세스 토큰으로 사용자 엔티티 조회
   *
   * @param request HTTP 요청 객체 (토큰 추출을 위함)
   * @return 사용자 엔티티
   * @throws GlobalException 사용자가 없을 경우
   */
  public UserEntity getUserByAccessToken(HttpServletRequest request) {
    return userRepository.findById(jwtUtil.getUserId(request))
        .orElseThrow(() -> new GlobalException(FailedResultCode.USER_NOT_FOUND));
  }

  /**
   * 사용자 ID로 사용자 엔티티 조회
   *
   * @param userId 조회할 사용자 ID
   * @return 사용자 엔티티
   * @throws GlobalException 사용자가 없을 경우
   */
  public UserEntity findByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(FailedResultCode.USER_NOT_FOUND));
  }

  /**
   * 사용자 닉네임 업데이트
   *
   * @param user 업데이트 대상 사용자
   * @param nickname 새 닉네임
   */
  public void updateUserNickname(UserEntity user, String nickname) {
    userRepository.updateUserNickname(user.getUserId(), nickname);
  }

  /**
   * 사용자 비밀번호 업데이트
   *
   * @param user 업데이트 대상 사용자
   * @param password 새 비밀번호 (암호화 전)
   */
  public void updateUserPassword(UserEntity user, String password) {
    String encodePassword = passwordEncoder.encode(password);
    userRepository.updateUserPassword(user.getUserId(), encodePassword);
  }
}