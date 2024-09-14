package com.zero.pennywise.jwt.filter;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.jwt.util.JwtUtil;
import com.zero.pennywise.model.request.account.UserDetailsDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String accessToken = request.getHeader("access");

    if (accessToken == null) {
      logger.info("Access token is null");
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // access 토큰이 만료가 되지 않은 경우
      jwtUtil.isExpired(accessToken);

      setAuthenticationFromToken(response, accessToken);
    } catch (ExpiredJwtException e) {
      expiredAccessToken(request, response);
      return;
    }

    filterChain.doFilter(request, response);
  }
  private void setAuthenticationFromToken(HttpServletResponse response, String token) {
    response.addHeader("access", token);

    Authentication authToken = getAuthToken(token);
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }


  // access 토큰이 만료되었을 경우 refresh 토큰 검증 후 재 발급
  private void expiredAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String refreshToken = getRefreshTokenFromCookies(request);

    if (refreshToken == null) {
      setResponse(response);
      return;
    }

    try {
      jwtUtil.isExpired(refreshToken);
      String newAccessToken = createNewAccessToken(refreshToken);

      setAuthenticationFromToken(response, newAccessToken);
    } catch (ExpiredJwtException e) {
      setResponse(response);
    }
  }

  // refresh 토큰 반환
  private String getRefreshTokenFromCookies(HttpServletRequest request) {
    return Arrays.stream(request.getCookies())
        .filter(cookie -> "refresh".equals(cookie.getName()))
        .map(Cookie::getValue)
        .findFirst()
        .orElse(null);
  }

  private Authentication getAuthToken(String token) {
    Long userId = jwtUtil.getUserId(token);
    String role = jwtUtil.getRole(token);

    UserEntity user = new UserEntity();
    user.setId(userId);
    user.setRole(role);

    UserDetailsDTO userDetails = new UserDetailsDTO(user);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  // access 토큰 재발급
  private String createNewAccessToken(String refreshToken) {
    Long userId = jwtUtil.getUserId(refreshToken);
    String role = jwtUtil.getRole(refreshToken);

    return jwtUtil.createJwt("access", userId, role);
  }

  // 오류 메세지 전송
  private void setResponse(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("인증 토큰이 만료되었습니다. 다시 로그인해 주세요.");
  }
}