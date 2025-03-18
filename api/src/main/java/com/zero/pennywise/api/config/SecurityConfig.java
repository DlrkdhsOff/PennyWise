package com.zero.pennywise.api.config;


import com.zero.pennywise.api.auth.jwt.JwtFilter;
import com.zero.pennywise.api.auth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationConfiguration authenticationConfiguration;
  private final JwtUtil jwtUtil;

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {

    return authenticationConfiguration.getAuthenticationManager();
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable);
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**"
            ).permitAll()
            .requestMatchers(
                "/",
                "/api/v1/users/signup",
                "/api/v1/users/login",
                "/api/v1/users/validate/email",
                "api/v1/users/validate/nickname"
            ).permitAll()
            .anyRequest().authenticated());
    http
        .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
    http
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

   return http.build();
  }

}
