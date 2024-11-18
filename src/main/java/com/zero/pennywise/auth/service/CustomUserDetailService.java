package com.zero.pennywise.auth.service;

import com.zero.pennywise.component.UserHandler;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.user.UserDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

  private final UserHandler userHandler;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    UserEntity user = userHandler.findByUserId(Long.parseLong(userId));

    return new UserDetailsDTO(user);
  }
}
