//package com.zero.pennywise.component;
//
//import com.zero.pennywise.entity.BalanceEntity;
//import com.zero.pennywise.entity.BudgetEntity;
//import com.zero.pennywise.entity.UserEntity;
//import com.zero.pennywise.model.response.budget.BudgetCache;
//import com.zero.pennywise.model.response.budget.Budgets;
//import com.zero.pennywise.repository.querydsl.budget.BudgetQueryRepository;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class RedisHandler {
//
//  private final CacheManager cacheManager;
//  private final BudgetQueryRepository budgetQueryRepository;
//
//  public BudgetCache getCachedBudgets(Long userId) {
//    Cache cache = cacheManager.getCache("Budgets");
//    if (cache != null) {
//      Cache.ValueWrapper cachedValue = cache.get(userId);
//      if (cachedValue != null) {
//        return (BudgetCache) cachedValue.get(); // 캐시에서 Budget 리스트 반환
//      }
//    }
//    return new BudgetCache(); // 캐시에 데이터가 없으면 null 반환
//  }
//
//  public void setOrUpdateBudgetAmount(Long userId, BudgetEntity budgetEntity) {
//    List<Budgets> budgetsList = getCachedBudgets(userId).getBudgets();
//
//    Budgets budgets = budgetQueryRepository.getBudget(budgetEntity.getUser(), budgetEntity.getCategory());
//    if (budgetsList != null) {
//      // budgetId가 일치하는 항목이 있는지 확인하고, 업데이트합니다.
//      boolean exists = budgetsList.stream()
//          .anyMatch(budget -> budget.getBudgetId().equals(budgets.getBudgetId()));
//
//      budgetsList = budgetsList.stream()
//          .map(budget -> budget.getBudgetId().equals(budgets.getBudgetId()) ? budgets : budget)
//          .collect(Collectors.toList());
//
//      // 일치하는 항목이 없다면 새 항목을 추가합니다.
//      if (!exists) {
//        budgetsList.add(budgets);
//      }
//    } else {
//      // budgetsList가 null이라면 새 리스트를 생성하고 항목 추가
//      budgetsList = new ArrayList<>();
//      budgetsList.add(budgets);
//    }
//
//    // 수정된 리스트를 캐시에 다시 저장합니다.
//    updateCache(userId, budgetsList);
//  }
//
//  private void updateCache(Long userId, List<Budgets> budgetsList) {
//    Cache cache = cacheManager.getCache("Budgets");
//    if (cache != null) {
//      cache.put(userId, new BudgetCache(budgetsList));
//    }
//  }
//
//  public void updateBalance(UserEntity user, BalanceEntity balance) {
//    List<Budgets> budgetsList = getCachedBudgets(user.getUserId()).getBudgets();
//
//    if (budgetsList != null) {
//      budgetsList = budgetsList.stream()
//          .map(budget -> {
//            // 카테고리 이름이 일치하는 경우 해당 객체를 Builder로 복사하여 amount 변경
//            if (budget.getCategoryName().equals(balance.getCategory().getCategoryName())) {
//              return Budgets.builder()
//                  .budgetId(budget.getBudgetId())
//                  .categoryName(budget.getCategoryName())
//                  .amount(budget.getAmount())
//                  .balance(budget.getAmount() - balance.getTotalExpensesAmount())
//                  .build();
//            }
//            return budget;
//          }).toList();
//
//      updateCache(user.getUserId(), budgetsList);
//    }
//  }
//}
