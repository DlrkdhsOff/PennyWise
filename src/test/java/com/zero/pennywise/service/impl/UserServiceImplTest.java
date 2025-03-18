//package com.zero.pennywise.service.impl;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//
//import com.zero.pennywise.auth.jwt.JwtUtil;
//import com.zero.pennywise.component.facade.UserFacade;
//import com.zero.pennywise.entity.UserEntity;
//import com.zero.pennywise.exception.GlobalException;
//import com.zero.pennywise.model.request.user.LoginDTO;
//import com.zero.pennywise.model.request.user.SignUpDTO;
//import com.zero.pennywise.model.response.ResultResponse;
//import com.zero.pennywise.model.type.FailedResultCode;
//import com.zero.pennywise.model.type.SuccessResultCode;
//import com.zero.pennywise.model.type.UserRole;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceImplTest {
//
//  @Mock
//  private UserFacade userFacade;
//
//  @Mock
//  private JwtUtil jwtUtil;
//
//  @Mock
//  private HttpServletRequest request;
//
//  @Mock
//  private HttpServletResponse response;
//
//  @InjectMocks
//  private UserServiceImpl userServiceImpl;
//
//  private final Long USER_ID = 1L;
//
//  private final String EMAIL = "user@example.com";
//  private final String PASSWORD = "123456";
//  private final String NEW_PASSWORD = "1098765";
//  private final String ENCODE_PASSWORD = "encodePassword";
//  private final String NICKNAME = "nickname";
//  private final UserRole USER_ROLE = UserRole.USER;
//  private final String ACCESS = "Access Token";
//  private final String REFRESH = "Refresh Token";
//
//
//  private UserEntity user;
//
//  private SignUpDTO signUpDTO;
//  private LoginDTO loginDTO;
//  private UpdateDTO updateDTO;
//
//
//  @BeforeEach
//  void setUp() {
//    user = UserEntity.builder()
//        .userId(USER_ID)
//        .email(EMAIL)
//        .password(ENCODE_PASSWORD)
//        .nickname(NICKNAME)
//        .role(USER_ROLE)
//        .build();
//
//    signUpDTO = new SignUpDTO(EMAIL, PASSWORD, NICKNAME);
//
//    loginDTO = new LoginDTO(EMAIL, PASSWORD);
//
//    updateDTO = new UpdateDTO(PASSWORD, NEW_PASSWORD, NICKNAME);
//  }
//
//  @Test
//  @DisplayName("회원가입 : 성공")
//  void signup() {
//    // given
//    when(userFacade.createUser(signUpDTO)).thenReturn(user);
//
//    // when
//    ResultResponse response = userServiceImpl.signup(signUpDTO);
//
//    // then
//    assertEquals(SuccessResultCode.SUCCESS_SIGNUP.getStatus(), response.getStatus());
//  }
//
//  @Test
//  @DisplayName("회원가입 : 실패 - 중복된 이메일")
//  void signup_Failed_ExistsEmail() {
//    // given
//    when(userFacade.createUser(signUpDTO))
//        .thenThrow(new GlobalException(FailedResultCode.EMAIL_ALREADY_USED));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> userServiceImpl.signup(signUpDTO));
//
//    // then
//    assertEquals(
//        FailedResultCode.EMAIL_ALREADY_USED.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("회원가입 : 실패 - 중복된 닉네임")
//  void signup_Failed_ExistsNickname() {
//    // given
//    when(userFacade.createUser(signUpDTO))
//        .thenThrow(new GlobalException(FailedResultCode.NICKNAME_ALREADY_USED));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> userServiceImpl.signup(signUpDTO));
//
//    // then
//    assertEquals(
//        FailedResultCode.NICKNAME_ALREADY_USED.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("로그인 : 성공")
//  void login() {
//    // given
//    when(userFacade.validateLoginData(loginDTO)).thenReturn(user);
//    when(jwtUtil.createJwt("access", user.getUserId(), USER_ROLE)).thenReturn(ACCESS);
//    when(jwtUtil.createJwt("refresh", user.getUserId(), USER_ROLE)).thenReturn(REFRESH);
//
//    // when
//    ResultResponse resultResponse = userServiceImpl.login(loginDTO, response);
//
//    // then
//    assertEquals(SuccessResultCode.SUCCESS_LOGIN.getStatus(), resultResponse.getStatus());
//  }
//
//  @Test
//  @DisplayName("로그인 : 실패 - 존재하지 않는 사용자")
//  void login_Failed_UserNotFound() {
//    // given
//    when(userFacade.validateLoginData(loginDTO)).
//        thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> userServiceImpl.login(loginDTO, response));
//
//    // then
//    assertEquals(
//        FailedResultCode.USER_NOT_FOUND.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("로그인 : 실패 - 비밀번호 불일치")
//  void login_Failed_PasswordMisMatch() {
//    // given
//    when(userFacade.validateLoginData(loginDTO)).
//        thenThrow(new GlobalException(FailedResultCode.PASSWORD_MISMATCH));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> userServiceImpl.login(loginDTO, response));
//
//    // then
//    assertEquals(
//        FailedResultCode.PASSWORD_MISMATCH.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("회원 정보 조회 : 성공")
//  void getUserInfo() {
//    // given
//    when(userFacade.getUserByAccessToken(request)).thenReturn(user);
//
//    // when
//    ResultResponse resultResponse = userServiceImpl.getUserInfo(request);
//
//    // then
//    assertEquals(SuccessResultCode.SUCCESS_GET_USER_INFO.getStatus(), resultResponse.getStatus());
//  }
//
//  @Test
//  @DisplayName("회원 정보 조회 : 실패")
//  void getUserInfo_Failed_UserNotFound() {
//    // given
//    when(userFacade.getUserByAccessToken(request))
//        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> userServiceImpl.getUserInfo(request));
//
//    // then
//    assertEquals(
//        FailedResultCode.USER_NOT_FOUND.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("회원 정보 수정 : 성공")
//  void updateUserInfo() {
//    // given
//    when(userFacade.updateUserInfo(request, updateDTO)).thenReturn(user);
//
//    // when
//    ResultResponse response = userServiceImpl.updateUserInfo(updateDTO, request);
//
//    // then
//    assertEquals(SuccessResultCode.SUCCESS_UPDATE_USER_INFO.getStatus(), response.getStatus());
//  }
//
//  @Test
//  @DisplayName("회원 정보 수정 : 실패 - 존재하지 않은 사용자")
//  void updateUserInfo_Failed_UserNotFound() {
//    // given
//    when(userFacade.updateUserInfo(request, updateDTO))
//        .thenThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> userServiceImpl.updateUserInfo(updateDTO, request));
//
//    // then
//    assertEquals(
//        FailedResultCode.USER_NOT_FOUND.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("회원 정보 수정 : 실패 - 비밀번호 불일치")
//  void updateUserInfo_Failed_PasswordMisMatch() {
//    // given
//    when(userFacade.updateUserInfo(request, updateDTO))
//        .thenThrow(new GlobalException(FailedResultCode.PASSWORD_MISMATCH));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> userServiceImpl.updateUserInfo(updateDTO, request));
//
//    // then
//    assertEquals(
//        FailedResultCode.PASSWORD_MISMATCH.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("회원 정보 수정 : 실패 - 중복된 닉네임")
//  void updateUserInfo_Failed_ExistsNickname() {
//    // given
//    when(userFacade.updateUserInfo(request, updateDTO))
//        .thenThrow(new GlobalException(FailedResultCode.NICKNAME_ALREADY_USED));
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> userServiceImpl.updateUserInfo(updateDTO, request));
//
//    // then
//    assertEquals(
//        FailedResultCode.NICKNAME_ALREADY_USED.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//
//  @Test
//  @DisplayName("회원 탈퇴 : 성공")
//  void deleteUser() {
//    // given & when
//    ResultResponse response = userServiceImpl.deleteUser(request);
//
//    // then
//    assertEquals(SuccessResultCode.SUCCESS_DELETE_USER_INFO.getStatus(), response.getStatus());
//  }
//
//  @Test
//  @DisplayName("회원 탈퇴 : 실패 - 존재하지 않은 사용자")
//  void deleteUser_Failed_UserNotFound() {
//    // given
//    doThrow(new GlobalException(FailedResultCode.USER_NOT_FOUND))
//        .when(userFacade).deleteAllUserData(request);
//
//    // when
//    GlobalException exception = assertThrows(GlobalException.class,
//        () -> userServiceImpl.deleteUser(request));
//
//    // then
//    assertEquals(
//        FailedResultCode.USER_NOT_FOUND.getStatus(),
//        exception.getResultResponse().getStatus()
//    );
//  }
//}