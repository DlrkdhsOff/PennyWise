package com.zero.pennywise.service;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.account.LoginDTO;
import com.zero.pennywise.model.request.account.RegisterDTO;
import com.zero.pennywise.model.request.account.UpdateDTO;
import com.zero.pennywise.model.request.budget.Balances;
import com.zero.pennywise.model.request.transaction.CategoryBalance;
import com.zero.pennywise.redis.BudgetCache;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.UserCategoryRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.WaringMessageRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final TransactionRepository transactionRepository;
  private final BudgetRepository budgetRepository;
  private final UserCategoryRepository userCategoryRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final WaringMessageRepository waringMessageRepository;
  private final BudgetCache budgetCache;

  // 회원 가입
  public String register(RegisterDTO registerDTO) {
    // 중복 아이디 체크
    if (userRepository.existsByEmail(registerDTO.getEmail())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디 입니다.");
    }

    validatePhoneNumber(registerDTO.getPhone());

    try {
      userRepository.save(RegisterDTO.of(registerDTO));
    } catch (Exception e) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 가입된 전화번호 입니다.");
    }

    return "회원가입 성공";
  }

  // 로그인
  public String login(LoginDTO loginDTO, HttpServletRequest request) {
    UserEntity user = userRepository.findByEmail(loginDTO.getEmail())
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 아이디 입니다."));

    // 비밀번호 일치 확인
    if (!user.getPassword().equals(loginDTO.getPassword())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
    }

    // 해당 사용자가 설정한 카테고리별 예산 남은 금액 레디스 데이터에 저장
    List<Balances> balances = getUserCategoryBalances(user);
    budgetCache.putBalanceInCache(user.getId(), balances);

    request.getSession().setAttribute("userId", user.getId());
    return "로그인 성공";
  }


  // 회원 정보 수정
  public String update(Long userId, UpdateDTO updateDTO) {
    UserEntity user = getUserById(userId);

    validatePhoneNumber(updateDTO.getPhone());

    user.setPassword(updateDTO.getPassword());
    user.setUsername(updateDTO.getUsername());
    user.setPhone(formatPhoneNumber(updateDTO.getPhone()));
    userRepository.save(user);

    return "회원 정보가 성공적으로 수정되었습니다.";
  }

  // 회원 탈퇴
  @Transactional
  public String delete(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "회원 탈퇴 실패하였습니다.");
    }

    budgetRepository.deleteAllByUserId(userId);
    transactionRepository.deleteAllByUserId(userId);
    userCategoryRepository.deleteAllByUserId(userId);
    waringMessageRepository.deleteAllByUserId(userId);
    userRepository.deleteById(userId);

    return "계정이 영구적으로 삭제 되었습니다.";
  }

  // 카테고리별 남은 금액
  private List<Balances> getUserCategoryBalances(UserEntity user) {
    List<BudgetEntity> userBudget = budgetRepository.findAllByUserId(user.getId());
    if (userBudget == null) {
      return null;
    }

    List<Balances> result = new ArrayList<>();

    for (BudgetEntity budget : userBudget) {
      result.add(getCategoryBalances(user.getId(), budget));
    }
    return result;
  }

  // 카테고리 남은 금액
  public Balances getCategoryBalances(Long userId, BudgetEntity budget) {
    Long categoryId = budget.getCategory().getCategoryId();
    String thisMonths = LocalDate.now().toString();

    CategoryBalance  categoryBalance = transactionQueryRepository
        .getTotalAmountByUserIdAndCategoryId(userId, categoryId, thisMonths);

    Long amount = budget.getAmount();
    String categoryName = categoryBalance.getCategoryName();
    Long balance = categoryBalance.getBalance();

    return new Balances(categoryBalance.getCategoryName(), amount, balance);
  }

  // 전화 번호 유효성 확인
  public void validatePhoneNumber(String phone) {
    if (!phone.matches("^[0-9]+$")) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "전화번호에 유효하지 않은 문자가 포함되어 있습니다.");
    } else if (phone.length() > 11) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "유효하지 않은 전화번호 입니다.");
    }
  }

  // 전화번호 formatting
  public String formatPhoneNumber(String phoneNumber) {
    return phoneNumber.substring(0, 3) + "-" +
        phoneNumber.substring(3, 7) + "-" +
        phoneNumber.substring(7);
  }

  // 공통 메서드: 사용자 조회
  private UserEntity getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));
  }
}