package com.zero.pennywise.config;

import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.jwt.filter.JwtFilter;
import com.zero.pennywise.jwt.filter.LoginFilter;
import com.zero.pennywise.jwt.util.JwtUtil;
import com.zero.pennywise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationConfiguration authenticationConfiguration;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final UserHandler userHandler;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

    return configuration.getAuthenticationManager();
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      BCryptPasswordEncoder passwordEncoder) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable);
    http
        .formLogin(AbstractHttpConfigurer::disable);
    http
        .httpBasic(AbstractHttpConfigurer::disable);

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "api/v1/register", "login").permitAll()
            .anyRequest().authenticated());

    http
        .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

    http
        .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)
            ,passwordEncoder, userHandler, jwtUtil), UsernamePasswordAuthenticationFilter.class);


    http
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

   return http.build();
  }

}
