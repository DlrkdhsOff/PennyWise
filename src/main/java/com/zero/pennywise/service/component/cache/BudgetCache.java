package com.zero.pennywise.service.component.cache;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.model.request.budget.BalancesDTO;
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
  public List<BalancesDTO> getBalancesFromCache(Long userId) {
    Cache cache = cacheManager.getCache("Balance");
    return cache != null ? cache.get(userId, List.class) : null;
  }

  // 캐시에 카테고리 목록 저장
  public void putBalanceInCache(Long userId, List<BalancesDTO> balances) {
    Cache cache = cacheManager.getCache("Balance");
    if (cache != null) {
      cache.put(userId, balances);
    }
  }

  // 생성한 예산 등록
  public void addNewBudget(Long userId, BudgetEntity budget, String categoryName) {
    List<BalancesDTO> balances = getBalancesFromCache(userId);

    if (balances != null) {
      balances.add(new BalancesDTO(categoryName, budget.getAmount(), budget.getAmount()));
      putBalanceInCache(userId, balances);
    }
  }

  // 예산 수정
  public void updateBudget(Long userId, Long totalAmount, String categoryName) {
    List<BalancesDTO> balances = getBalancesFromCache(userId);

    for (BalancesDTO balance : balances) {
      if (balance.getCategoryName().equals(categoryName)) {
        balance.setAmount(totalAmount);
      }
    }
    putBalanceInCache(userId, balances);
  }

  // 예산 삭제
  public void deleteBalance(Long userId, String categoryName) {
    List<BalancesDTO> balances = getBalancesFromCache(userId);

    balances.removeIf(balance -> balance.getCategoryName().equals(categoryName));

    // 삭제 후 캐시에 업데이트
    putBalanceInCache(userId, balances);
  }

  // Balances 객체 반환
  public BalancesDTO getBalances(Long userId, String categoryName) {
    List<BalancesDTO> balances = getBalancesFromCache(userId);

    for (BalancesDTO balance : balances) {
      if (balance.getCategoryName().equals(categoryName)) {
        return balance;
      }
    }
    return null;
  }

  // 남은 금액 수정
  public void updateBalance(Long userId, Long totalBalance, String categoryName) {
    List<BalancesDTO> balances = getBalancesFromCache(userId);

    for (BalancesDTO balance : balances) {
      if (balance.getCategoryName().equals(categoryName)) {
        balance.setBalance(totalBalance);
      }
    }
    putBalanceInCache(userId, balances);
  }
}

