package com.zero.pennywise.service.impl;

import com.zero.pennywise.auth.jwt.JwtUtil;
import com.zero.pennywise.component.UserHandler;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.user.LoginDTO;
import com.zero.pennywise.model.request.user.SignUpDTO;
import com.zero.pennywise.model.request.user.UpdateDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.model.response.user.UserInfo;
import com.zero.pennywise.model.type.SuccessResultCode;
import com.zero.pennywise.model.type.TokenType;
import com.zero.pennywise.model.type.UserRole;
import com.zero.pennywise.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserHandler userHandler;
  private final JwtUtil jwtUtil;

  // 요청 헤더에서 사용자 정보를 추출하여 반환
  private UserEntity fetchUser(HttpServletRequest request) {
    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    return userHandler.findByUserId(userId);
  }

  // 회원 가입
  @Override
  public ResultResponse signup(SignUpDTO signUpDTO) {

    userHandler.validateEmail(signUpDTO.getEmail());
    userHandler.validateNickname(signUpDTO.getNickname());

    UserEntity user = SignUpDTO.of(signUpDTO, userHandler.encodePassword(signUpDTO.getPassword()));

    userHandler.saveUser(user);

    return ResultResponse.of(SuccessResultCode.SUCCESS_SIGNUP);
  }

  // 로그인
  @Override
  public ResultResponse login(LoginDTO loginDTO, HttpServletResponse response) {

    UserEntity user = userHandler.findByEmail(loginDTO.getEmail());
    userHandler.validatePassword(loginDTO.getPassword());

    String access = jwtUtil.createJwt("access", user.getUserId(), UserRole.USER);
    String refresh = jwtUtil.createJwt("refresh", user.getUserId(), UserRole.USER);

    response.addHeader(TokenType.ACCESS.getValue(), "Bearer " + access);
    response.addCookie(jwtUtil.setCookie(refresh));

    return ResultResponse.of(SuccessResultCode.SUCCESS_LOGIN);
  }

  // 회원 정보 조회
  @Override
  public ResultResponse getUserInfo(HttpServletRequest request) {
    UserEntity user = fetchUser(request);

    UserInfo userInfo = new UserInfo(user.getEmail(), user.getNickname());

    return new ResultResponse(SuccessResultCode.SUCCESS_GET_USER_INFO, userInfo);
  }

  // 회원 정보 수정
  @Override
  public ResultResponse updateUserInfo(UpdateDTO updateDTO, HttpServletRequest request) {

    UserEntity user = fetchUser(request);

    userHandler.validatePassword(updateDTO.getBeforePassword());
    userHandler.validateNickname(updateDTO.getNickname());

    UserEntity updateUser = UpdateDTO.of(user, updateDTO,
        userHandler.encodePassword(updateDTO.getAfterPassword()));

    userHandler.saveUser(updateUser);


    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_USER_INFO);
  }

  // 회원 정보 삭제
  @Override
  @Transactional
  public ResultResponse deleteUser(HttpServletRequest request) {
    UserEntity user = fetchUser(request);

    userHandler.deleteAllUserData(user);

    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_USER_INFO);
  }
}