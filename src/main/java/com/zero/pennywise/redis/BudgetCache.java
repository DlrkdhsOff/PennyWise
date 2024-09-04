package com.zero.pennywise.redis;

import com.zero.pennywise.model.request.budget.Balances;
import com.zero.pennywise.model.response.Categories;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BudgetCache {

  private final CacheManager cacheManager;

  // 캐시에 카테고리 목록 저장
  public void putBalanceInCache(Long userId, List<Balances> balances) {
    Cache cache = cacheManager.getCache("Balance");
    if (cache != null) {
      cache.put(userId, balances);
    }
  }

}
