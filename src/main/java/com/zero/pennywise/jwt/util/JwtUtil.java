package com.zero.pennywise.jwt.util;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final SecretKey secretKey;

  public JwtUtil(@Value("${spring.jwt.secret}")String secret) {

    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public Long getUserId(String token) {
    return Jwts.parser().verifyWith(secretKey).build()
        .parseSignedClaims(token)
        .getPayload()
        .get("userId", Long.class);
  }

  public Boolean isExpired(String token) {
    return Jwts.parser().verifyWith(secretKey).build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration().before(new Date());
  }

  public String createJwt(Long userId, String role, Long expiredMs) {

    System.out.println(new Date(System.currentTimeMillis()));
    System.out.println(new Date(System.currentTimeMillis() + expiredMs));
    return Jwts.builder()
        .claim("userId", userId)
        .claim("role", role)
        .notBefore(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiredMs))
        .signWith(secretKey)
        .compact();
  }
}
