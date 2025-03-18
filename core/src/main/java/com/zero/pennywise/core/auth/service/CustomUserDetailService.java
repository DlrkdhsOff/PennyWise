package com.zero.pennywise.core.auth.service;

import com.zero.pennywise.domain.entity.UserEntity;
import com.zero.pennywise.core.exception.GlobalException;
import com.zero.pennywise.domain.model.type.FailedResultCode;
import com.zero.pennywise.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByUserId(Long.parseLong(userId))
        .orElseThrow(() -> new GlobalException(FailedResultCode.USER_NOT_FOUND));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password("")
        .roles(user.getRole().toString())
        .build();
  }
}
