package com.zero.pennywise.controller;

import com.zero.pennywise.model.request.user.LoginDTO;
import com.zero.pennywise.model.request.user.SignUpDTO;
import com.zero.pennywise.model.response.ResultResponse;
import com.zero.pennywise.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 관련 API를 처리하는 컨트롤러
 * 회원가입, 로그인, 사용자 정보 조회 및 수정 기능 제공
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  /**
   * 이메일 중복 검증
   *
   * @param email 검증할 이메일
   * @return 검증 결과
   */
  @GetMapping("/validate/email")
  public ResponseEntity<ResultResponse> validateEmail(@RequestParam("email") String email) {
    ResultResponse response = userService.validateEmail(email);
    return new ResponseEntity<>(response, response.getStatus());
  }

  /**
   * 닉네임 중복 검증
   *
   * @param nickname 검증할 닉네임
   * @return 검증 결과
   */
  @GetMapping("/validate/nickname")
  public ResponseEntity<ResultResponse> validateNickname(@RequestParam("nickname") String nickname) {
    ResultResponse response = userService.validateNickname(nickname);
    return new ResponseEntity<>(response, response.getStatus());
  }

  /**
   * 비밀번호 유효성 검증
   *
   * @param request HTTP 요청 객체
   * @param password 검증할 비밀번호
   * @return 검증 결과
   */
  @GetMapping("/validate/password")
  public ResponseEntity<ResultResponse> validatePassword(
      HttpServletRequest request,
      @RequestParam("password") String password) {
    ResultResponse response = userService.validatePassword(request, password);
    return new ResponseEntity<>(response, response.getStatus());
  }

  /**
   * 회원가입 처리
   *
   * @param signUpDTO 회원가입 정보
   * @return 회원가입 결과
   */
  @PostMapping("/signup")
  public ResponseEntity<ResultResponse> register(@RequestBody @Valid SignUpDTO signUpDTO) {
    ResultResponse resultResponse = userService.signup(signUpDTO);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  /**
   * 로그인 처리
   *
   * @param response HTTP 응답 객체 (쿠키나 헤더를 설정하기 위함)
   * @param loginDTO 로그인 정보
   * @return 로그인 결과
   */
  @PostMapping("/login")
  public ResponseEntity<ResultResponse> login(
      HttpServletResponse response,
      @RequestBody @Valid LoginDTO loginDTO) {
    ResultResponse resultResponse = userService.login(loginDTO, response);
    return new ResponseEntity<>(resultResponse, resultResponse.getStatus());
  }

  /**
   * 사용자 정보 조회
   *
   * @param request HTTP 요청 객체 (인증 정보 추출을 위함)
   * @return 사용자 정보
   */
  @GetMapping("/info")
  public ResponseEntity<ResultResponse> getUserInfo(HttpServletRequest request) {
    ResultResponse response = userService.getUserInfo(request);
    return new ResponseEntity<>(response, response.getStatus());
  }

  /**
   * 닉네임 수정
   *
   * @param request HTTP 요청 객체 (인증 정보 추출을 위함)
   * @param nickname 새 닉네임
   * @return 수정 결과
   */
  @PutMapping("/update/nickname")
  public ResponseEntity<ResultResponse> updateNickname(
      HttpServletRequest request,
      @RequestParam("nickname") String nickname) {
    ResultResponse response = userService.updateNickname(request, nickname);
    return new ResponseEntity<>(response, response.getStatus());
  }

  /**
   * 비밀번호 수정
   *
   * @param request HTTP 요청 객체 (인증 정보 추출을 위함)
   * @param password 새 비밀번호
   * @return 수정 결과
   */
  @PutMapping("/update/password")
  public ResponseEntity<ResultResponse> updatePassword(
      HttpServletRequest request,
      @RequestParam("password") String password) {
    ResultResponse response = userService.updatePassword(request, password);
    return new ResponseEntity<>(response, response.getStatus());
  }
}