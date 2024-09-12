package com.zero.pennywise.service;

import com.zero.pennywise.component.cache.BudgetCache;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.account.RegisterDTO;
import com.zero.pennywise.model.request.account.UpdateDTO;
import com.zero.pennywise.model.request.account.UserDetailsDTO;
import com.zero.pennywise.model.request.budget.BalancesDTO;
import com.zero.pennywise.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final BudgetCache budgetCache;
  private final UserHandler userHandler;
  private final BCryptPasswordEncoder passwordEncoder;
  private final Logger logger = LoggerFactory.getLogger(UserService.class);

  // 회원 가입
  public String register(RegisterDTO registerDTO) {
    userHandler.validateEmail(registerDTO.getEmail());
    userHandler.validatePhoneNumber(registerDTO.getPhone());
    registerDTO.setPhone(userHandler.formatPhoneNumber(registerDTO.getPhone()));

    registerDTO.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
    userRepository.save(RegisterDTO.of(registerDTO));

    return "회원가입 성공";
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws GlobalException {

    UserEntity user = userHandler.getUserByEmail(email);

    logger.info("user = {}", user.toString());

      return new UserDetailsDTO(user);

  }

  // 회원 정보 수정
  public String update(Long userId, UpdateDTO updateDTO) {
    UserEntity user = userHandler.getUserById(userId);

    userHandler.validatePhoneNumber(updateDTO.getPhone());

    user.setPassword(updateDTO.getPassword());
    user.setUsername(updateDTO.getUsername());
    user.setPhone(userHandler.formatPhoneNumber(updateDTO.getPhone()));

    userRepository.save(user);
    return "회원 정보가 성공적으로 수정되었습니다.";
  }

  // 회원 탈퇴
  @Transactional
  public String delete(Long userId) {
    UserEntity user = userHandler.getUserById(userId);

    userHandler.deleteOtherData(user.getId());
    userRepository.deleteById(userId);

    return "계정이 영구적으로 삭제 되었습니다.";
  }
}