package com.zero.pennywise.controller;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.account.RegisterDTO;
import com.zero.pennywise.model.request.account.UpdateDTO;
import com.zero.pennywise.service.UserService;
import com.zero.pennywise.utils.UserAuthorizationUtil;
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

  // 회원 정보 수정
  @PatchMapping("/account")
  public ResponseEntity<String> updateAccount(@RequestBody @Valid UpdateDTO updateDTO) {
    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.ok()
        .body(userService.update(userId, updateDTO));
  }

  // 회원 탈퇴
  @DeleteMapping("/account")
  public ResponseEntity<String> delete() {
    Long userId = UserAuthorizationUtil.getLoginUserId();

    return ResponseEntity.ok()
        .body(userService.delete(userId));
  }
}