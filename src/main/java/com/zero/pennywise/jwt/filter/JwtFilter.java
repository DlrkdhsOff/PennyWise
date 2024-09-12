package com.zero.pennywise.jwt.filter;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.jwt.util.JwtUtil;
import com.zero.pennywise.model.request.account.UserDetailsDTO;
import com.zero.pennywise.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {


    String authorization = request.getHeader("Authorization");

    if (authorization == null || !authorization.startsWith("Bearer ")) {

      logger.info("token is null");
      filterChain.doFilter(request, response);
      return;
    }

    String token = authorization.split(" ")[1];

    if (jwtUtil.isExpired(token)) {

      logger.info("token isExpired");
      filterChain.doFilter(request, response);
      return;
    }

    Long userId = jwtUtil.getUserId(token);

    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 회원입니다."));


    UserDetailsDTO userDetails = new UserDetailsDTO(user);


    Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }

}
