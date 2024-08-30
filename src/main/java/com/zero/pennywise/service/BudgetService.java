package com.zero.pennywise.service;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.budget.BudgetDTO;
import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.model.entity.UserEntity;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {

  private final CategoriesRepository categoriesRepository;
  private final BudgetRepository budgetRepository;
  private final UserRepository userRepository;


  // 카테고리별 예산 설정
  public String setBudget(Long userId, BudgetDTO budgetDTO) {

    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 회원 입니다."));
    
    return categoriesRepository.findByCategoryName(budgetDTO.getCategoryName())
        .map(category -> existingCategory(user, category, budgetDTO.getAmount()))
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다."));
  }

  private String existingCategory(UserEntity user, CategoriesEntity category, Long amount) {
    if (budgetRepository.existsByUserIdAndCategoryCategoryId(user.getId(),
        category.getCategoryId())) {

      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 등록한 예산입니다.");
    }

    budgetRepository.save(BudgetEntity.builder()
        .user(user)
        .category(category)
        .amount(amount)
        .build());

    return "성공적으로 예산을 등록하였습니다.";
  }

  // 카테고리별 예산 수정
  @Transactional
  public String updateBudget(Long userId, BudgetDTO budgetDTO) {
    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 회원입니다."));

    String categoryName = budgetDTO.getCategoryName();
    CategoriesEntity category = categoriesRepository.findByCategoryName(categoryName)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리입니다."));

    BudgetEntity budget = budgetRepository.findByUserIdAndCategoryCategoryId(userId, category.getCategoryId())
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "예산이 등록되지 않은 카테고리입니다."));

    budget.setAmount(budgetDTO.getAmount());
    budgetRepository.save(budget);

    return "성공적으로 예산을 수정하였습니다.";
  }
}