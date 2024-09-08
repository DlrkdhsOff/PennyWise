package com.zero.pennywise.service;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.account.LoginDTO;
import com.zero.pennywise.model.request.account.RegisterDTO;
import com.zero.pennywise.model.request.account.UpdateDTO;
import com.zero.pennywise.model.request.budget.BalancesDTO;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.service.component.cache.BudgetCache;
import com.zero.pennywise.service.component.handler.UserHandler;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BudgetCache budgetCache;
  private final UserHandler userHandler;

  // 회원 가입
  public String register(RegisterDTO registerDTO) {
    userHandler.validateEmail(registerDTO.getEmail());
    userHandler.validatePhoneNumber(registerDTO.getPhone());
    registerDTO.setPhone(userHandler.formatPhoneNumber(registerDTO.getPhone()));

    try {
      userRepository.save(RegisterDTO.of(registerDTO));
    } catch (Exception e) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 가입된 전화번호 입니다.");
    }

    return "회원가입 성공";
  }

  // 로그인
  public String login(LoginDTO loginDTO, HttpServletRequest request) {
    UserEntity user = userHandler.getUserByEmail(loginDTO.getEmail());
    userHandler.validatePassword(user, loginDTO.getPassword());

//    List<BalancesDTO> balances = userHandler.getUserCategoryBalances(user);
//    budgetCache.putBalanceInCache(user.getId(), balances);

    request.getSession().setAttribute("userId", user.getId());
    return "로그인 성공";
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