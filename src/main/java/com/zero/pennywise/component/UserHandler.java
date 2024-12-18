package com.zero.pennywise.component;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.repository.BalanceRepository;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoryRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.WaringMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHandler {

  private final BCryptPasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final BudgetRepository budgetRepository;
  private final CategoryRepository categoryRepository;
  private final TransactionRepository transactionRepository;
  private final BalanceRepository balanceRepository;
  private final WaringMessageRepository waringMessageRepository;

  // 이메일 검증
  public void validateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new GlobalException(FailedResultCode.EMAIL_ALREADY_USED);
    }
  }

  // 비밀번호 암호화
  public String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }

  // 이메일과 일치하는 User 객체 반환
  public UserEntity findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new GlobalException(FailedResultCode.USER_NOT_FOUND));
  }

  // 비밀번호 검증
  public void validatePassword(String password) {
    if (!passwordEncoder.matches(password, encodePassword(password))) {
      throw new GlobalException(FailedResultCode.PASSWORD_MISMATCH);
    }
  }

  // userId와 일치하는 User 객체 반환
  public UserEntity findByUserId(long userId) {
    return userRepository.findByUserId(userId)
        .orElseThrow(() -> new GlobalException(FailedResultCode.USER_NOT_FOUND));
  }

  // 닉네임 검증
  public void validateNickname(String nickname) {
    if (userRepository.existsByNickname(nickname)) {
      throw new GlobalException(FailedResultCode.NICKNAME_ALREADY_USED);
    }
  }

  // 회원 정보 저장
  public void saveUser(UserEntity user) {
    userRepository.save(user);
  }

  // 회원 탈퇴시 나머지 데이터 삭제
  public void deleteAllUserData(UserEntity user) {
    budgetRepository.deleteAllByUser(user);
    balanceRepository.deleteAllByUser(user);
    transactionRepository.deleteAllByUser(user);
    categoryRepository.deleteAllByUser(user);
    waringMessageRepository.deleteAllByUser(user);
    userRepository.delete(user);

  }


}
