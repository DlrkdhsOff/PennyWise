package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.budget.BudgetDTO;
import com.zero.pennywise.model.entity.BudgetEntity;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.model.entity.UserEntity;
import com.zero.pennywise.model.response.BudgetPage;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.querydsl.BudgetQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {

  private final CategoriesRepository categoriesRepository;
  private final BudgetRepository budgetRepository;
  private final UserRepository userRepository;
  private final BudgetQueryRepository budgetQueryRepository;


  // 카테고리별 예산 설정
  public String setBudget(Long userId, BudgetDTO budgetDTO) {

    UserEntity user = getUserById(userId);
    
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
    UserEntity user = getUserById(userId);

    CategoriesEntity category = getCategoryByName(budgetDTO.getCategoryName());

    BudgetEntity budget = getBudgetByUserIdAndCategoryId(user.getId(), category.getCategoryId());

    budget.setAmount(budgetDTO.getAmount());
    budgetRepository.save(budget);

    return "성공적으로 예산을 수정하였습니다.";
  }

  public BudgetPage getBudget(Long userId, Pageable page) {
    UserEntity user = getUserById(userId);

    Pageable pageable = page(page);

    return BudgetPage.of(budgetQueryRepository.findAllBudgetByUserId(userId, pageable));
  }

  public String deleteBudget(Long userId, String categoryName) {
    UserEntity user = getUserById(userId);

    CategoriesEntity category = getCategoryByName(categoryName);

    BudgetEntity budget = getBudgetByUserIdAndCategoryId(user.getId(), category.getCategoryId());

    budgetRepository.deleteByBudgetId(budget.getBudgetId());

    return "예산을 성공적으로 삭제 하였습닏.";
  }

  // 공통 메서드: 사용자 조회
  private UserEntity getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));
  }

  // 공통 메서드: 카테고리 조회
  private CategoriesEntity getCategoryByName(String categoryName) {
    return categoriesRepository.findByCategoryName(categoryName)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다."));
  }

  // 공통 메서드: 예산 조회
  private BudgetEntity getBudgetByUserIdAndCategoryId(Long userId, Long categoryId) {
    return budgetRepository.findByUserIdAndCategoryCategoryId(userId, categoryId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "예산이 등록되지 않은 카테고리입니다."));
  }

}