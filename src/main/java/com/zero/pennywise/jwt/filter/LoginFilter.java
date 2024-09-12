package com.zero.pennywise.jwt.filter;

import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.jwt.util.JwtUtil;
import com.zero.pennywise.model.request.account.UserDetailsDTO;
import com.zero.pennywise.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final UserHandler userHandler;
  private final JwtUtil jwtUtil;
  public final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    String email = request.getParameter("email");
    String password = obtainPassword(request);

    logger.info("email = {}", request.getParameter("email"));
    logger.info("password = {}", obtainPassword(request));

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        email, password, null);

    return authenticationManager.authenticate(authToken);
  }


  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) throws IOException {

    UserDetailsDTO userDetails = (UserDetailsDTO) authentication.getPrincipal();

    logger.info("userDetails : {}", userDetails.getUserId());

    UserEntity user = userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 회원입니다."));


    String token = jwtUtil.createJwt(user.getId(), user.getRole());
    userHandler.getUserCategoryBalances(user);

    response.addHeader("Authorization", "Bearer " + token);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(user.getUsername() + "님 안녕하세요");
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) throws IOException {

    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    String errorMessage = null;

    Optional<UserEntity> optionalUser = userRepository.findByEmail(request.getParameter("email"));

    if (optionalUser.isEmpty()) {
      errorMessage = "존재하지 않은 아이디 입니다.";
    } else {
      errorMessage = "비밀 번호를 다시 입력해주세요";

    }

    // 응답 본문에 메시지를 작성
    response.getWriter().write(errorMessage);
  }
}
