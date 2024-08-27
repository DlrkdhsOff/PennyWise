package com.zero.pennywise.service;

import com.zero.pennywise.model.dto.LoginDTO;
import com.zero.pennywise.model.dto.RegisterDTO;
import com.zero.pennywise.model.dto.UpdateDTO;
import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.model.entity.UserEntity;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.budget.BudgetRepository;
import com.zero.pennywise.status.AccountStatus;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final CategoriesRepository categoriesRepository;
  private final BudgetRepository budgetRepository;

  // 회원 가입
  public Response register(RegisterDTO registerDTO) {

    // 중복 아이디 체크
    if (userRepository.existsByEmail(registerDTO.getEmail())) {
      return new Response(AccountStatus.REGISTER_FAILED_DUPLICATE_ID);
    }

    Response response = validatePhoneNumber(registerDTO.getPhone());

    if (response != null) {
      return response;
    } else {
      registerDTO.setPhone(registerDTO.getPhone());
    }

    UserEntity user = RegisterDTO.of(registerDTO);
    userRepository.save(user);

    createDefaultBudget(user.getId());

    return new Response(AccountStatus.REGISTER_SUCCESS);
  }

  // 로그인
  public Response login(LoginDTO loginDTO, HttpServletRequest request) {

    // 아이디 존재여부 확인
    if (!userRepository.existsByEmail(loginDTO.getEmail())) {
      return new Response(AccountStatus.USER_NOT_FOUND);
    }

    UserEntity user = userRepository.findByEmail(loginDTO.getEmail());

    // 비밀번호 일치 확인
    if (!user.getPassword().equals(loginDTO.getPassword())) {
      return new Response(AccountStatus.PASSWORD_DOES_NOT_MATCH);
    }

    request.getSession().setAttribute("userId", user.getId());
    return new Response(AccountStatus.LOGIN_SUCCESS);
  }

  // 회원 정보 수정
  public Response update(Long userId, UpdateDTO updateDTO) {
    // 회원 정보 조회
    Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);

    if (optionalUserEntity.isEmpty()) {
      return new Response(AccountStatus.USER_NOT_FOUND);
    }

    UserEntity user = optionalUserEntity.get();

    if (updateDTO == null || StringUtils.hasText(updateDTO.getPassword())
        && StringUtils.hasText(updateDTO.getUsername())
        && StringUtils.hasText(updateDTO.getPhone())) {
      return new Response(AccountStatus.PARAMETER_IS_NULL);
    }

    // 비밀번호 업데이트
    if (!StringUtils.hasText(updateDTO.getPassword())) {
      user.setPassword(updateDTO.getPassword());
    }

    // 사용자 이름 업데이트
    if (!StringUtils.hasText(updateDTO.getUsername())) {
      user.setUsername(updateDTO.getUsername());
    }

    // 전화번호 업데이트
    if (!StringUtils.hasText(updateDTO.getPhone())) {
      Response validationResponse = validatePhoneNumber(updateDTO.getPhone());
      if (validationResponse != null) {
        return validationResponse;
      }
      user.setPhone(formatPhoneNumber(updateDTO.getPhone()));
    }
    userRepository.save(user);

    return new Response(AccountStatus.UPDATE_SUCCESS);
  }

  // 회원 탈퇴
  public Response delete(Long userId) {

    if (!userRepository.existsById(userId)) {
      userRepository.deleteById(userId);
      return new Response(AccountStatus.ACCOUNT_DELETION_SUCCESS);
    }

    return new Response(AccountStatus.ACCOUNT_DELETION_FAILED);
  }

  // 회원 가입시 기본 예산 생성
  public void createDefaultBudget(Long userId) {
    List<CategoriesEntity> categoryList = categoriesRepository.findAllBySharedIsTrue();

    for (CategoriesEntity categories : categoryList) {
      budgetRepository.save(BudgetEntity.builder()
          .userId(userId)
          .categoryId(categories.getCategoryId())
          .build());
    }
  }

  // 전화 번호 유효성 확인
  public Response validatePhoneNumber(String phone) {
    if (!phone.matches("^[0-9]+$")) {
      return new Response(AccountStatus.PHONE_NUMBER_CONTAINS_INVALID_CHARACTERS);
    } else if (phone.length() > 11) {
      return new Response(AccountStatus.PHONE_NUMBER_INVALID);
    }

    return null;
  }

  // 전화번호 formatting
  public String formatPhoneNumber(String phoneNumber) {
    return phoneNumber.substring(0, 3) + "-" +
        phoneNumber.substring(3, 7) + "-" +
        phoneNumber.substring(7);
  }
}
