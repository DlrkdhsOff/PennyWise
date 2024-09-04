package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.getPagedBalanceData;
import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.budget.Balances;
import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.response.BudgetPage;
import com.zero.pennywise.redis.BudgetCache;
import com.zero.pennywise.redis.CategoryCache;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.querydsl.category.CategoryQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {

  private final BudgetRepository budgetRepository;
  private final UserRepository userRepository;
  private final CategoryQueryRepository categoryQueryRepository;
  private final CategoryCache categoryCache;
  private final BudgetCache budgetCache;

  // 카테고리별 예산 설정
  public String setBudget(Long userId, BudgetDTO budgetDTO) {
    UserEntity user = getUserById(userId);
    CategoriesEntity category = getCategory(user.getId(), budgetDTO.getCategoryName());

    if (budgetRepository.existsByUserIdAndCategoryCategoryId(user.getId(), category.getCategoryId())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "이미 등록한 예산 입니다.");
    }

    BudgetEntity budget = budgetRepository.save(BudgetEntity.builder()
        .user(user)
        .category(category)
        .amount(budgetDTO.getAmount())
        .build());

    List<Balances> balances = budgetCache.getBalancesFromCache(userId);
    budgetCache.putNewBudgetInCache(balances, user.getId(), budget, category.getCategoryName());

    return "성공적으로 예산을 등록하였습니다.";
  }

  // 카테고리별 예산 수정
  @Transactional
  public String updateBudget(Long userId, BudgetDTO budgetDTO) {
    UserEntity user = getUserById(userId);
    CategoriesEntity category = getCategory(user.getId(), budgetDTO.getCategoryName());

    BudgetEntity budget = getBudgetByUserIdAndCategoryId(user.getId(), category.getCategoryId());
    budget.setAmount(budgetDTO.getAmount());

    budgetRepository.save(budget);

    List<Balances> balances = budgetCache.getBalancesFromCache(userId);
    budgetCache.updateCategory(balances, user.getId(), budget, budgetDTO.getCategoryName());

    return "성공적으로 예산을 수정하였습니다.";
  }

  // 예산 목록 조회
  public BudgetPage getBudget(Long userId, Pageable page) {
    UserEntity user = getUserById(userId);
    Pageable pageable = page(page);

    List<Balances> balances = budgetCache.getBalancesFromCache(userId);
    return BudgetPage.of(getPagedBalanceData(balances, pageable));
  }

  // 예산 삭제
  @Transactional
  public String deleteBudget(Long userId, String categoryName) {
    UserEntity user = getUserById(userId);
    CategoriesEntity category = getCategory(user.getId(), categoryName);

    BudgetEntity budget = getBudgetByUserIdAndCategoryId(user.getId(), category.getCategoryId());
    budgetRepository.deleteByBudgetId(budget.getBudgetId());

    List<Balances> balances = budgetCache.getBalancesFromCache(user.getId());
    budgetCache.deleteBalance(balances, user.getId(), categoryName);

    return "예산을 성공적으로 삭제하였습니다.";
  }

  // 사용자 조회
  private UserEntity getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));
  }

  // 예산 조회
  private BudgetEntity getBudgetByUserIdAndCategoryId(Long userId, Long categoryId) {
    return budgetRepository.findByUserIdAndCategoryCategoryId(userId, categoryId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "예산이 등록되지 않은 카테고리입니다."));
  }

  // 사용자의 모든 카테고리 정보를 캐시에 저장
  private CategoriesEntity getCategory(Long userId, String categoryName) {
    List<CategoriesEntity> categories = categoryCache.getCategoriesFromCache(userId);

    if (categories == null) {
      categories = categoryQueryRepository.getAllCategory(userId);
      categoryCache.putCategoriesInCache(userId, categories);
    }

    return categoryCache.getCategory(categories, categoryName);
  }
}