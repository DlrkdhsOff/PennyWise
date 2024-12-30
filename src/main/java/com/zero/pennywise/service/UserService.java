package com.zero.pennywise.service;

import com.zero.pennywise.model.request.user.LoginDTO;
import com.zero.pennywise.model.request.user.SignUpDTO;
import com.zero.pennywise.model.request.user.UpdateDTO;
import com.zero.pennywise.model.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

  ResultResponse signup(SignUpDTO registerDTO);

  ResultResponse login(LoginDTO loginDTO, HttpServletResponse response);

  ResultResponse getUserInfo(HttpServletRequest request);

  ResultResponse updateUserInfo(UpdateDTO updateDTO, HttpServletRequest request);

  ResultResponse deleteUser(HttpServletRequest request);
}
