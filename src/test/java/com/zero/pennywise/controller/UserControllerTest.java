package com.zero.pennywise.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.zero.pennywise.model.dto.LoginDTO;
import com.zero.pennywise.model.dto.RegisterDTO;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.service.UserService;
import com.zero.pennywise.status.AccountStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private RegisterDTO successRegister;
  private RegisterDTO duplicate;
  private RegisterDTO hasChar;
  private RegisterDTO lengthsOver;
  private LoginDTO successLogin;
  private LoginDTO notFoundUser;
  private LoginDTO pwUnMatch;


  @BeforeEach
  void setUp() {
    // Mockito 초기화
    MockitoAnnotations.openMocks(this);

    // 회원가입 성공
    successRegister = new RegisterDTO("valid@example.com", "ValidPassword123", "이름", "010-1111-1111");
    // 중복 아이디인 경우
    duplicate = new RegisterDTO("user","ValidPassword123", "이름", "010-1111-1111");
    // 전화번호에 숫자가 아닌 문자가 있는 경우
    hasChar = new RegisterDTO("user1","ValidPassword123", "이름", "010-1111-1111");
    // 전화번호가 11자리 이상일 경우
    lengthsOver = new RegisterDTO("user1","ValidPassword123", "이름", "010111111111");

    // 로그인 성공
    successLogin = new LoginDTO("user", "0000");
    // 존재하지 않는 아이디
    notFoundUser = new LoginDTO("user1", "0000");
    // 비밀번호 불일치
    pwUnMatch = new LoginDTO("user", "1111");

  }

  @Test
  void testRegisterSuccess() {
    // given
    when(userService.register(successRegister)).thenReturn(new Response(AccountStatus.REGISTER_SUCCESS));

    // when
    ResponseEntity<?> response = userController.register(successRegister);

    // then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(AccountStatus.REGISTER_SUCCESS.getMessage(), response.getBody());
  }

  @Test
  void testDuplicateUserId() {
    // given
    when(userService.register(duplicate)).thenReturn(new Response(AccountStatus.REGISTER_FAILED_DUPLICATE_ID));

    // when
    ResponseEntity<?> response = userController.register(duplicate);

    // then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(AccountStatus.REGISTER_FAILED_DUPLICATE_ID.getMessage(), response.getBody());
  }

  @Test
  void testPhoneNumberInvalidCharacter() {
    // given
    when(userService.register(hasChar)).thenReturn(new Response(AccountStatus.PHONE_NUMBER_CONTAINS_INVALID_CHARACTERS));

    // when
    ResponseEntity<?> response = userController.register(hasChar);

    // then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(AccountStatus.PHONE_NUMBER_CONTAINS_INVALID_CHARACTERS.getMessage(), response.getBody());
  }

  @Test
  void testPhoneNumberInvalid() {
    // given
    when(userService.register(lengthsOver)).thenReturn(new Response(AccountStatus.PHONE_NUMBER_INVALID));

    // when
    ResponseEntity<?> response = userController.register(lengthsOver);

    // then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(AccountStatus.PHONE_NUMBER_INVALID.getMessage(), response.getBody());
  }

  @Test
  void testSuccessLogin() {
    // given
    HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
    HttpSession mockSession = Mockito.mock(HttpSession.class);

    when(userService.login(successLogin)).thenReturn(new Response(AccountStatus.LOGIN_SUCCESS));
    when(mockRequest.getSession()).thenReturn(mockSession);

    // when
    ResponseEntity<?> response = userController.login(successLogin, mockRequest);

    // then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(AccountStatus.LOGIN_SUCCESS.getMessage(), response.getBody());
  }

  @Test
  void testNotFoundUser() {
    // given
    HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
    HttpSession mockSession = Mockito.mock(HttpSession.class);

    when(userService.login(notFoundUser)).thenReturn(new Response(AccountStatus.USER_NOT_FOUND));
    when(mockRequest.getSession()).thenReturn(mockSession);

    // when
    ResponseEntity<?> response = userController.login(notFoundUser, mockRequest);

    // then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(AccountStatus.USER_NOT_FOUND.getMessage(), response.getBody());
  }

  @Test
  void testPwUnMatch() {
    // given
    HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
    HttpSession mockSession = Mockito.mock(HttpSession.class);

    when(userService.login(pwUnMatch)).thenReturn(new Response(AccountStatus.PASSWORD_DOES_NOT_MATCH));
    when(mockRequest.getSession()).thenReturn(mockSession);

    // when
    ResponseEntity<?> response = userController.login(pwUnMatch, mockRequest);

    // then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(AccountStatus.PASSWORD_DOES_NOT_MATCH.getMessage(), response.getBody());
  }
}