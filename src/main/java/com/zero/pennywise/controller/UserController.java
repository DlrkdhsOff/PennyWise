package com.zero.pennywise.controller;

import com.zero.pennywise.model.request.user.LoginDTO;
import com.zero.pennywise.model.request.user.RegisterDTO;
import com.zero.pennywise.model.request.user.UpdateDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<ResultResponse> register(@RequestBody @Valid RegisterDTO registerDTO) {

    ResultResponse resultResponse = userService.signup(registerDTO);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<ResultResponse> login(
      @RequestBody @Valid LoginDTO loginDTO,
      HttpServletResponse response) {

    ResultResponse resultResponse = userService.login(loginDTO, response);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  // 회원정보 조회
  @GetMapping
  public ResponseEntity<ResultResponse> getUserInfo(HttpServletRequest request) {

    ResultResponse resultResponse = userService.getUserInfo(request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  // 회원 정보 수정
  @PutMapping
  public ResponseEntity<ResultResponse> updateUserInfo(
      @RequestBody @Valid UpdateDTO updateDTO,
      HttpServletRequest request) {

    ResultResponse resultResponse = userService.updateUserInfo(updateDTO, request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  // 회원 탈퇴
  @DeleteMapping
  public ResponseEntity<ResultResponse> deleteUser(HttpServletRequest request) {

    ResultResponse resultResponse = userService.deleteUser(request);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }
  
}