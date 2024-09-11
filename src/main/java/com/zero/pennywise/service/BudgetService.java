package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.getPagedBalanceData;

import com.zero.pennywise.component.cache.BudgetCache;
import com.zero.pennywise.component.handler.BudgetHandler;
import com.zero.pennywise.component.handler.CategoryHandler;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.budget.BalancesDTO;
import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.response.budget.BudgetPage;
import com.zero.pennywise.repository.BudgetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {

  private final UserHandler userHandler;
  private final BudgetCache budgetCache;
  private final BudgetHandler budgetHandler;
  private final CategoryHandler categoryHandler;
  private final BudgetRepository budgetRepository;

  // 카테고리별 예산 설정
  public String setBudget(Long userId, BudgetDTO budgetDTO) {
    UserEntity user = userHandler.getUserById(userId);

    CategoryEntity category =categoryHandler
        .getCateogry(user.getId(), budgetDTO.getCategoryName());

    budgetHandler.validateBudget(user.getId(), category.getCategoryId());
    BudgetEntity budget = budgetHandler.save(user, category, budgetDTO.getAmount());

    budgetCache.addNewBudget(user.getId(), budget, category.getCategoryName());
    return "성공적으로 예산을 등록하였습니다.";
  }

  // 카테고리별 예산 목록 조회
  public BudgetPage getBudget(Long userId, Pageable page) {
    UserEntity user = userHandler.getUserById(userId);

    List<BalancesDTO> balances = budgetCache.getBalancesFromCache(userId);
    return BudgetPage.of(getPagedBalanceData(balances, page));
  }

  // 카테고리별 예산 수정
  @Transactional
  public String updateBudget(Long userId, BudgetDTO budgetDTO) {
    UserEntity user = userHandler.getUserById(userId);

    CategoryEntity category = categoryHandler
        .getCateogry(user.getId(), budgetDTO.getCategoryName());

    BudgetEntity budget = budgetHandler.getBudget(user.getId(), category.getCategoryId());

    budget.setAmount(budgetDTO.getAmount());
    budgetRepository.save(budget);

    budgetCache.updateBudget(user.getId(), budget.getAmount(), budgetDTO.getCategoryName());
    return "성공적으로 예산을 수정하였습니다.";
  }

  // 예산 삭제
  @Transactional
  public String deleteBudget(Long userId, String categoryName) {
    UserEntity user = userHandler.getUserById(userId);

    CategoryEntity category = categoryHandler.getCateogry(user.getId(), categoryName);

    BudgetEntity budget = budgetHandler.getBudget(user.getId(), category.getCategoryId());
    budgetRepository.deleteByBudgetId(budget.getBudgetId());

    budgetCache.deleteBalance(userId, category.getCategoryName());
    return "성공적으로 예산을 삭제 하였습니다.";
  }
}