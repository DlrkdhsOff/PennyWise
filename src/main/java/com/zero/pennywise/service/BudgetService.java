package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.getPagedBalanceData;
import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.budget.BalancesDTO;
import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.response.budget.BudgetPage;
import com.zero.pennywise.repository.BudgetRepository;
import com.zero.pennywise.service.component.handler.BudgetHandler;
import com.zero.pennywise.service.component.handler.UserHandler;
import com.zero.pennywise.service.component.cache.BudgetCache;
import com.zero.pennywise.service.component.cache.CategoryCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {

  private final BudgetRepository budgetRepository;
  private final UserHandler userHandler;
  private final CategoryCache categoryCache;
  private final BudgetCache budgetCache;
  private final BudgetHandler budgetHandler;

  // 카테고리별 예산 설정
  public String setBudget(Long userId, BudgetDTO budgetDTO) {
    UserEntity user = userHandler.getUserById(userId);

    CategoriesEntity category = categoryCache
        .getCategoryByCategoryName(userId, budgetDTO.getCategoryName());

    budgetHandler.validateBudget(user.getId(), category.getCategoryId());

    BudgetEntity budget = budgetRepository.save(
        BudgetEntity.builder()
            .user(user)
            .category(category)
            .amount(budgetDTO.getAmount())
            .build()
    );

    budgetCache.addNewBudget(user.getId(), budget, category.getCategoryName());

    return "성공적으로 예산을 등록하였습니다.";
  }

  // 카테고리별 예산 수정
  @Transactional
  public String updateBudget(Long userId, BudgetDTO budgetDTO) {
    UserEntity user = userHandler.getUserById(userId);

    CategoriesEntity category = categoryCache
        .getCategoryByCategoryName(user.getId(), budgetDTO.getCategoryName());

    BudgetEntity budget = budgetHandler
        .getBudgetByUserIdAndCategoryId(user.getId(), category.getCategoryId());

    budget.setAmount(budgetDTO.getAmount());

    budgetRepository.save(budget);
    budgetCache.updateBudget(user.getId(), budget.getAmount(), budgetDTO.getCategoryName());

    return "성공적으로 예산을 수정하였습니다.";
  }

  // 예산 목록 조회
  public BudgetPage getBudget(Long userId, Pageable page) {
    UserEntity user = userHandler.getUserById(userId);
    Pageable pageable = page(page);

    List<BalancesDTO> balances = budgetCache.getBalancesFromCache(userId);
    return BudgetPage.of(getPagedBalanceData(balances, pageable));
  }

  // 예산 삭제
  @Transactional
  public String deleteBudget(Long userId, String categoryName) {
    UserEntity user = userHandler.getUserById(userId);

    CategoriesEntity category = categoryCache
        .getCategoryByCategoryName(user.getId(), categoryName);

    BudgetEntity budget = budgetHandler
        .getBudgetByUserIdAndCategoryId(user.getId(), category.getCategoryId());

    budgetRepository.deleteByBudgetId(budget.getBudgetId());
    budgetCache.deleteBalance(user.getId(), categoryName);

    return "예산을 성공적으로 삭제하였습니다.";
  }
}