package com.zero.pennywise.api.service.impl;

import com.zero.pennywise.api.component.UserFacade;
import com.zero.pennywise.domain.entity.UserEntity;
import com.zero.pennywise.domain.model.request.user.LoginDTO;
import com.zero.pennywise.domain.model.request.user.SignUpDTO;
import com.zero.pennywise.domain.model.response.ResultResponse;
import com.zero.pennywise.domain.model.response.user.UserInfo;
import com.zero.pennywise.domain.model.type.SuccessResultCode;
import com.zero.pennywise.api.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 서비스 구현체
 * 사용자 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserFacade userFacade;

  /**
   * 이메일 중복 검증
   *
   * @param email 검증할 이메일
   * @return 검증 결과 응답
   */
  @Override
  public ResultResponse validateEmail(String email) {
    String response = userFacade.validateEmail(email);
    return new ResultResponse(SuccessResultCode.VALIDATE_EMAIL, response);
  }

  /**
   * 닉네임 중복 검증
   *
   * @param nickname 검증할 닉네임
   * @return 검증 결과 응답
   */
  @Override
  public ResultResponse validateNickname(String nickname) {
    String response = userFacade.validateNickname(nickname);
    return new ResultResponse(SuccessResultCode.VALIDATE_NICKNAME, response);
  }

  /**
   * 비밀번호 유효성 검증
   *
   * @param request HTTP 요청 객체 (인증 정보 추출을 위함)
   * @param password 검증할 비밀번호
   * @return 검증 결과 응답
   */
  @Override
  public ResultResponse validatePassword(HttpServletRequest request, String password) {
    UserEntity user = userFacade.getUserByAccessToken(request);
    String response = userFacade.validatePassword(user, password);
    return new ResultResponse(SuccessResultCode.VALIDATE_PASSWORD, response);
  }

  /**
   * 회원가입 처리
   *
   * @param signUpDTO 회원가입 정보
   * @return 회원가입 결과 응답
   */
  @Override
  public ResultResponse signup(SignUpDTO signUpDTO) {
    userFacade.createAndSaveUser(signUpDTO);
    return ResultResponse.of(SuccessResultCode.SUCCESS_SIGNUP);
  }

  /**
   * 로그인 처리
   *
   * @param loginDTO 로그인 정보
   * @param response HTTP 응답 객체 (토큰 설정을 위함)
   * @return 로그인 결과 응답
   */
  @Override
  public ResultResponse login(LoginDTO loginDTO, HttpServletResponse response) {
    UserEntity user = userFacade.validateLoginData(loginDTO);
    userFacade.generateToken(response, user);
    return ResultResponse.of(SuccessResultCode.SUCCESS_LOGIN);
  }

  /**
   * 사용자 정보 조회
   *
   * @param request HTTP 요청 객체 (인증 정보 추출을 위함)
   * @return 사용자 정보 응답
   */
  @Override
  public ResultResponse getUserInfo(HttpServletRequest request) {
    UserEntity user = userFacade.getUserByAccessToken(request);
    UserInfo userInfo = new UserInfo(user.getEmail(), user.getNickname());
    return new ResultResponse(SuccessResultCode.SUCCESS_GET_USER_INFO, userInfo);
  }

  /**
   * 닉네임 수정
   *
   * @param request HTTP 요청 객체 (인증 정보 추출을 위함)
   * @param nickname 새 닉네임
   * @return 수정 결과 응답
   */
  @Override
  public ResultResponse updateNickname(HttpServletRequest request, String nickname) {
    UserEntity user = userFacade.getUserByAccessToken(request);
    userFacade.updateUserNickname(user, nickname);
    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_USER_INFO);
  }

  /**
   * 비밀번호 수정
   *
   * @param request HTTP 요청 객체 (인증 정보 추출을 위함)
   * @param password 새 비밀번호
   * @return 수정 결과 응답
   */
  @Override
  public ResultResponse updatePassword(HttpServletRequest request, String password) {
    UserEntity user = userFacade.getUserByAccessToken(request);
    userFacade.updateUserPassword(user, password);
    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_USER_INFO);
  }
}