package com.zero.pennywise.controller;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.account.LoginDTO;
import com.zero.pennywise.model.dto.account.RegisterDTO;
import com.zero.pennywise.model.dto.account.UpdateDTO;
import com.zero.pennywise.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

  private final UserService userService;

  // 회원가입
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO registerDTO) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.register(registerDTO));
  }

  // 로그인
  @PostMapping(value = "/login")
  public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO,
      HttpServletRequest request) {

    return ResponseEntity.ok()
        .body(userService.login(loginDTO, request));
  }


  // 회원 정보 수정
  @PatchMapping( "/account")
  public ResponseEntity<String> updateAccount(@RequestBody @Valid UpdateDTO updateDTO,
      HttpServletRequest request) {

    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }

    return ResponseEntity.ok()
        .body(userService.update(userId, updateDTO));
  }

  // 회원 탈퇴
  @DeleteMapping("/account")
  public ResponseEntity<String> delete(HttpServletRequest request) {
    Long userId = (Long) request.getSession().getAttribute("userId");

    if (userId == null) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "로그인을 해주세요");
    }
    return ResponseEntity.ok().body(userService.delete(userId));
  }
}


