package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoryRepository;
import com.zero.pennywise.repository.SavingsRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.WaringMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHandler {

  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;
  private final BudgetRepository budgetRepository;
  private final CategoryRepository categoryRepository;
  private final WaringMessageRepository waringMessageRepository;
  private final SavingsRepository savingsRepository;

  public void validateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디 입니다.");
    }
  }

  public UserEntity getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));
  }

  public UserEntity findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElse(null);
  }

  // 전화 번호 유효성 확인
  public void validatePhoneNumber(String phone) {
    if (!phone.matches("^[0-9]+$")) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "전화번호에 유효하지 않은 문자가 포함되어 있습니다.");
    } else if (phone.length() > 11) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "유효하지 않은 전화번호 입니다.");
    }
  }

  // 아이디와 일치하는 UserEntity 객체 반환
  public UserEntity getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 아이디 입니다."));
  }

  // 전화번호 formatting
  public String formatPhoneNumber(String phoneNumber) {
    return phoneNumber.substring(0, 3) + "-" +
        phoneNumber.substring(3, 7) + "-" +
        phoneNumber.substring(7);
  }

  // 회원 탈퇴시 나머지 데이터 삭제
  public void deleteOtherData(Long userId) {
    budgetRepository.deleteAllByUserId(userId);
    transactionRepository.deleteAllByUserId(userId);
    categoryRepository.deleteAllByUserId(userId);
    waringMessageRepository.deleteAllByUserId(userId);
    savingsRepository.deleteAllByUserId(userId);

  }
}
