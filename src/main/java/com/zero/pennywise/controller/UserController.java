package com.zero.pennywise.controller;

import com.zero.pennywise.model.dto.LoginDTO;
import com.zero.pennywise.model.dto.RegisterDTO;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  // 회원가입
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO registerDTO) {
    Response result = userService.register(registerDTO);

    return ResponseEntity.status(result.getStatus()).body(result.getMessage());
  }

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO,
      HttpServletRequest request) {
    Response result = userService.login(loginDTO);

    if (result.getStatus() == HttpStatus.OK) {
      request.getSession().setAttribute("userId", loginDTO.getUserId());
    }

    return ResponseEntity.status(result.getStatus()).body(result.getMessage());
  }

}
