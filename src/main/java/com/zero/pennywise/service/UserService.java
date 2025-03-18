package com.zero.pennywise.service;

import com.zero.pennywise.model.request.user.LoginDTO;
import com.zero.pennywise.model.request.user.SignUpDTO;
import com.zero.pennywise.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

  ResultResponse validateEmail(String email);

  ResultResponse validateNickname(String nickname);

  ResultResponse validatePassword(HttpServletRequest request, String password);

  ResultResponse signup(SignUpDTO registerDTO);

  ResultResponse login(LoginDTO loginDTO, HttpServletResponse response);

  ResultResponse getUserInfo(HttpServletRequest request);

  ResultResponse updateNickname(HttpServletRequest request, String nickname);

  ResultResponse updatePassword(HttpServletRequest request, String password);
}
