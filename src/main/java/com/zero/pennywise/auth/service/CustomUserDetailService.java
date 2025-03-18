package com.zero.pennywise.auth.service;

import com.zero.pennywise.component.facade.UserFacade;
import com.zero.pennywise.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

  private final UserFacade userFacade;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    UserEntity user = userFacade.findByUserId(Long.parseLong(userId));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password("")
        .roles(user.getRole().toString())
        .build();
  }
}
