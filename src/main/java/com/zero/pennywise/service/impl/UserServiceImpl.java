package com.zero.pennywise.service.impl;

import com.zero.pennywise.auth.jwt.JwtUtil;
import com.zero.pennywise.component.facade.FacadeManager;
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

  private final FacadeManager facadeManager;
  private final JwtUtil jwtUtil;

  // 회원 가입
  @Override
  public ResultResponse signup(SignUpDTO signUpDTO) {

    UserEntity user = facadeManager.createUser(signUpDTO);

    facadeManager.saveUser(user);

    return ResultResponse.of(SuccessResultCode.SUCCESS_SIGNUP);
  }

  // 로그인
  @Override
  public ResultResponse login(LoginDTO loginDTO, HttpServletResponse response) {

    UserEntity user = facadeManager.validateLoginData(loginDTO);

    String access = jwtUtil.createJwt("access", user.getUserId(), UserRole.USER);
    String refresh = jwtUtil.createJwt("refresh", user.getUserId(), UserRole.USER);

    response.addHeader(TokenType.ACCESS.getValue(), "Bearer " + access);
    response.addCookie(jwtUtil.setCookie(refresh));

    return ResultResponse.of(SuccessResultCode.SUCCESS_LOGIN);
  }

  // 회원 정보 조회
  @Override
  public ResultResponse getUserInfo(HttpServletRequest request) {
    UserEntity user = facadeManager.getUserByAccessToken(request);

    UserInfo userInfo = new UserInfo(user.getEmail(), user.getNickname());

    return new ResultResponse(SuccessResultCode.SUCCESS_GET_USER_INFO, userInfo);
  }

  // 회원 정보 수정
  @Override
  public ResultResponse updateUserInfo(UpdateDTO updateDTO, HttpServletRequest request) {
    UserEntity user = facadeManager.updateUserInfo(request, updateDTO);

    facadeManager.saveUser(user);


    return ResultResponse.of(SuccessResultCode.SUCCESS_UPDATE_USER_INFO);
  }

  // 회원 정보 삭제
  @Override
  @Transactional
  public ResultResponse deleteUser(HttpServletRequest request) {
    facadeManager.deleteAllUserData(request);

    return ResultResponse.of(SuccessResultCode.SUCCESS_DELETE_USER_INFO);
  }
}