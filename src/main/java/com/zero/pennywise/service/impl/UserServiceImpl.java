package com.zero.pennywise.service.impl;

import com.zero.pennywise.auth.jwt.JwtUtil;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.user.LoginDTO;
import com.zero.pennywise.model.request.user.RegisterDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
  private final UserHandler userHandler;
  private final JwtUtil jwtUtil;

  // 회원 가입
  @Override
  public ResultResponse signup(RegisterDTO registerDTO) {

    userHandler.validateEmail(registerDTO.getEmail());

    UserEntity user = UserEntity.builder()
        .email(registerDTO.getEmail())
        .password(userHandler.encodePassword(registerDTO.getPassword()))
        .nickname(registerDTO.getNickname())
        .role(UserRole.USER)
        .build();

    userHandler.saveUser(user);

    return ResultResponse.of(SuccessResultCode.SUCCESS_SIGNUP);
  }

  // 로그인
  @Override
  public ResultResponse login(LoginDTO loginDTO, HttpServletResponse response) {

    UserEntity user = userHandler.findByEmail(loginDTO.getEmail());
    userHandler.validatePassword(loginDTO.getPassword());

    String access = jwtUtil.createJwt("access", user.getId(), UserRole.USER);
    String refresh = jwtUtil.createJwt("refresh", user.getId(), UserRole.USER);

    response.addHeader(TokenType.ACCESS.getValue(), "Bearer " + access);
    response.addCookie(jwtUtil.setCookie(refresh));

    return ResultResponse.of(SuccessResultCode.SUCCESS_LOGIN);
  }

  // 회원 정보 조회
  @Override
  public ResultResponse getUserInfo(HttpServletRequest request) {
    log.info("-----------------");
    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    UserEntity user = userHandler.findByUserId(userId);

    UserInfo userInfo = new UserInfo(user.getEmail(), user.getNickname());

    return new ResultResponse(SuccessResultCode.SUCCESS_GET_USER_INFO, userInfo);
  }

  // 회원 정보 수정
  @Override
  public ResultResponse updateUserInfo(UpdateDTO updateDTO, HttpServletRequest request) {

    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    UserEntity user = userHandler.findByUserId(userId);

    userHandler.validatePassword(updateDTO.getBeforePassword());
    userHandler.validateNickname(updateDTO.getNickname());

    UserEntity updateUser = UserEntity.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(userHandler.encodePassword(updateDTO.getAfterPassword()))
        .nickname(updateDTO.getNickname())
        .role(user.getRole())
        .build();

    userHandler.saveUser(updateUser);


    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_USER_INFO);
  }

  // 회원 정보 삭제
  @Override
  @Transactional
  public ResultResponse deleteUser(HttpServletRequest request) {
    Long userId = jwtUtil.getUserId(request.getHeader(TokenType.ACCESS.getValue()));
    UserEntity user = userHandler.findByUserId(userId);

    userHandler.deleteAllUserData(user.getId());

    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_USER_INFO);
  }
}