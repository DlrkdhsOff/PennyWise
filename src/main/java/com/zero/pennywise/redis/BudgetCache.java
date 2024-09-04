package com.zero.pennywise.redis;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.model.request.budget.Balances;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetCache {

  private final CacheManager cacheManager;

  @SuppressWarnings("unchecked")
  public List<Balances> getBalancesFromCache(Long userId) {
    Cache cache = cacheManager.getCache("Balance");
    return cache != null ? cache.get(userId, List.class) : null;
  }

  // 캐시에 카테고리 목록 저장
  public void putBalanceInCache(Long userId, List<Balances> balances) {
    Cache cache = cacheManager.getCache("Balance");
    if (cache != null) {
      cache.put(userId, balances);
    }
  }

  // 생성한 예산 등록
  public void putNewBudgetInCache(List<Balances> balances,
      Long userId, BudgetEntity budget, String categoryName) {

    if (balances != null) {
      balances.add(new Balances(categoryName, budget.getAmount(), budget.getAmount()));
      putBalanceInCache(userId, balances);
    }
  }

  // 예산 수정
  public void updateCategory(List<Balances> balances,
      Long userId, BudgetEntity budget, String categoryName) {

    for (Balances balance : balances) {
      if (balance.getCategoryName().equals(categoryName)) {
        balance.setCategoryName(categoryName);
        balance.setAmount(budget.getAmount());
      }
    }
    putBalanceInCache(userId, balances);
  }

  // 예산 삭제
  public void deleteBalance(List<Balances> balances, Long userId, String categoryName) {
    balances.removeIf(balance -> balance.getCategoryName().equals(categoryName));

    // 삭제 후 캐시에 업데이트
    putBalanceInCache(userId, balances);
  }
}

