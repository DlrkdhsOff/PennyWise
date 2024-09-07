package com.zero.pennywise.service;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.model.request.transaction.CategoryBalanceDTO;
import com.zero.pennywise.model.response.AnalyzeDTO;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import com.zero.pennywise.service.component.redis.CategoryCache;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyzeService {

  private final TransactionRepository transactionRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final CategoryCache categoryCache;


  public Object analyze(Long userId) {

    // 지난 3달 지출 금액
    AnalyzeDTO lastThreeMonth = getLastThreeMonthBalance(userId);

    // 이번달 지출 금액
    AnalyzeDTO thisMonth = getThisMonthBalance(userId);

    // 이번달 지출금액이 지난 3달간 평균 지출 금액보다 150% 이상일 경우
    if (lastThreeMonth.getTotalExpenses() * 2.5 < thisMonth.getTotalExpenses()) {

    }
    return null;
  }


  // 이번달 전체 지출 금액 및 카테고리별 지출 금액
  private AnalyzeDTO getThisMonthBalance(Long userId) {
    List<CategoriesEntity> categories = categoryCache.getCategoriesFromCache(userId);

    String thisMonth = LocalDate.now().toString();

    Long totalEexpenses = transactionQueryRepository.getExpenses(userId, null, thisMonth);

    List<CategoryBalanceDTO> list = new ArrayList<>();
    for (CategoriesEntity c : categories) {
      list.add(new CategoryBalanceDTO(
          c.getCategoryName(),
          transactionQueryRepository.getExpenses(userId, c.getCategoryId(), thisMonth)
      ));
    }

    return new AnalyzeDTO(totalEexpenses, list);
  }

  // 지난 3달 지출 총액 평균 및 카테고리별 지출 총액 평균
  private AnalyzeDTO getLastThreeMonthBalance(Long userId) {
    List<CategoriesEntity> categories = categoryCache.getCategoriesFromCache(userId);

    LocalDateTime startDateTime = getStartDate();
    LocalDateTime endDateTime = getLastMonth();

    Long totalEexpenses = transactionQueryRepository.getTracsactionAvgLastThreeMonth(userId, null, startDateTime, endDateTime);

    List<CategoryBalanceDTO> list = new ArrayList<>();
    for (CategoriesEntity c : categories) {
      list.add(new CategoryBalanceDTO(
          c.getCategoryName(),
          transactionQueryRepository.getTracsactionAvgLastThreeMonth(userId, c.getCategoryId(), startDateTime, endDateTime)
      ));
    }

    return new AnalyzeDTO(totalEexpenses, list);
  }

  private LocalDateTime getStartDate() {
    return LocalDateTime.now().minusMonths(4).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
  }

  private LocalDateTime getLastMonth() {
    YearMonth lastMonth = YearMonth.from(LocalDateTime.now().minusMonths(1));
    return lastMonth.atEndOfMonth().atTime(23, 59, 59);
  }
}
