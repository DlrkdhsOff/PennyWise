package com.zero.pennywise.component.handler;

import com.zero.pennywise.model.response.budget.Budgets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHandler {

  private final CacheManager cacheManager;

  @SuppressWarnings("unchecked")
  public List<Budgets> getCachedBudgets(Long userId) {
    Cache cache = cacheManager.getCache("Budgets");
    if (cache != null) {
      Cache.ValueWrapper cachedValue = cache.get(userId);
      if (cachedValue != null) {
        return (List<Budgets>) cachedValue.get(); // 캐시에서 Budget 리스트 반환
      }
    }
    return null; // 캐시에 데이터가 없으면 null 반환
  }

  public void setOrUpdateBudgetAmount(Long userId, Budgets budgets) {
    List<Budgets> budgetsList = getCachedBudgets(userId);

    if (budgetsList != null) {
      // budgetId가 일치하는 항목이 있는지 확인하고, 업데이트합니다.
      boolean exists = budgetsList.stream()
          .anyMatch(budget -> budget.getBudgetId().equals(budgets.getBudgetId()));

      budgetsList = budgetsList.stream()
          .map(budget -> budget.getBudgetId().equals(budgets.getBudgetId()) ? budgets : budget)
          .collect(Collectors.toList());

      // 일치하는 항목이 없다면 새 항목을 추가합니다.
      if (!exists) {
        budgetsList.add(budgets);
      }
    } else {
      // budgetsList가 null이라면 새 리스트를 생성하고 항목 추가
      budgetsList = new ArrayList<>();
      budgetsList.add(budgets);
    }

    // 수정된 리스트를 캐시에 다시 저장합니다.
    updateCache(userId, budgetsList);
  }

  private void updateCache(Long userId, List<Budgets> budgetsList) {
    Cache cache = cacheManager.getCache("Budgets");
    if (cache != null) {
      cache.put(userId, budgetsList);
    }
  }
}
