package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.getPagedBalanceData;
import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.budget.BalancesDTO;
import com.zero.pennywise.model.request.budget.BudgetDTO;
import com.zero.pennywise.model.response.budget.BudgetPage;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.service.component.cache.BudgetCache;
import com.zero.pennywise.service.component.handler.BudgetHandler;
import com.zero.pennywise.service.component.handler.UserHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {

  private final CategoriesRepository categoriesRepository;
  private final UserHandler userHandler;
  private final BudgetCache budgetCache;
  private final BudgetHandler budgetHandler;

  // 카테고리별 예산 설정
  public String setBudget(Long userId, BudgetDTO budgetDTO) {
    UserEntity user = userHandler.getUserById(userId);

    return categoriesRepository.findByUserIdAndCategoryName(user.getId(), budgetDTO.getCategoryName())
        .map(category -> {
          budgetHandler.validateBudget(user.getId(), category.getCategoryId());
          BudgetEntity budget = budgetHandler.save(user, category, budgetDTO.getAmount());

          budgetCache.addNewBudget(user.getId(), budget, category.getCategoryName());
          return "성공적으로 예산을 등록하였습니다.";
        })
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));
  }


  // 카테고리별 예산 수정
  public String updateBudget(Long userId, BudgetDTO budgetDTO) {
    UserEntity user = userHandler.getUserById(userId);

    CategoriesEntity category = categoriesRepository
        .findByUserIdAndCategoryName(user.getId(), budgetDTO.getCategoryName())
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));

    return budgetHandler.updateBudget(user, category, budgetDTO);
  }


  // 카테고리별 예산 목록 조회
  public BudgetPage getBudget(Long userId, Pageable page) {
    UserEntity user = userHandler.getUserById(userId);
    Pageable pageable = page(page);

    List<BalancesDTO> balances = budgetCache.getBalancesFromCache(userId);
    return BudgetPage.of(getPagedBalanceData(balances, pageable));
  }


  // 예산 삭제
  public String deleteBudget(Long userId, String categoryName) {
    UserEntity user = userHandler.getUserById(userId);

    CategoriesEntity category = categoriesRepository.findByUserIdAndCategoryName(user.getId(), categoryName)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."));

    return budgetHandler.deleteBudget(user.getId(), category);
  }
}