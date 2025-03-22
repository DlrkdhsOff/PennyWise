package com.zero.pennywise.core.service.impl;

import static com.zero.pennywise.domain.utils.FormatUtil.formatWon;

import com.zero.pennywise.core.component.FinanceFacade;
import com.zero.pennywise.core.component.UserFacade;
import com.zero.pennywise.core.service.AnalyzeService;
import com.zero.pennywise.domain.entity.TransactionEntity;
import com.zero.pennywise.domain.entity.UserEntity;
import com.zero.pennywise.domain.model.response.ResultResponse;
import com.zero.pennywise.domain.model.type.AnalyzeType;
import com.zero.pennywise.domain.model.type.SuccessResultCode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyzeServiceImpl implements AnalyzeService {

  private final UserFacade userFacade;
  private final FinanceFacade financeFacade;

  @Override
  public ResultResponse analyzeIncomeAndExpenses(HttpServletRequest request) {
    UserEntity user = userFacade.getUserByAccessToken(request);

    List<String> categories = financeFacade.getCategoryList(user);
    List<TransactionEntity> thisTList = financeFacade.findTransactionByType(user, AnalyzeType.THIS);
    List<TransactionEntity> lastTList = financeFacade.findTransactionByType(user, AnalyzeType.LAST);

    Map<String, Map<String, String>> analyzeMap = analyzeResults(thisTList, lastTList, categories);

    return new ResultResponse(SuccessResultCode.SUCCESS_ANALYZE, analyzeMap);
  }

  private Map<String, Map<String, String>> analyzeResults(List<TransactionEntity> thisTList, List<TransactionEntity> lastTList, List<String> categories) {

    Map<String, Map<String, String>> analyzeMap = new HashMap<>();

    analyzeMap.put("이번 달 총 수입/지출 금액", getTotalAmount(thisTList, categories, AnalyzeType.THIS));
    analyzeMap.put("이번 달 카테고리 별 수입 금액", getThisCategoryAmount(thisTList, categories, AnalyzeType.INCOME));
    analyzeMap.put("이번 달 카테고리 별 지출 금액", getThisCategoryAmount(thisTList, categories, AnalyzeType.EXPENSES));

    analyzeMap.put("최근 3개월 평균 수입/지출 금액", getTotalAmount(lastTList, categories, AnalyzeType.LAST));
    analyzeMap.put("최근 3개월 카테고리 별 수입 금액", getLastCategoryAmount(lastTList, categories, AnalyzeType.LAST_INCOME));
    analyzeMap.put("최근 3개월 카테고리 별 지출 금액", getLastCategoryAmount(lastTList, categories, AnalyzeType.LAST_EXPENSES));

    return analyzeMap;
  }

  private Map<String, String> getTotalAmount(List<TransactionEntity> tList, List<String> categories, AnalyzeType type) {

    long totalIncome = 0L;
    long totalExpense = 0L;

    if (!tList.isEmpty()) {
      for (TransactionEntity t : tList) {
        totalIncome += t.getTotalIncomeAmount();
        totalExpense += t.getTotalExpensesAmount();
      }

      if (type.equals(AnalyzeType.LAST)) {
        totalIncome /= 3;
        totalExpense /= 3;
      }
    }

    Map<String, String> totalMap = new HashMap<>();
    totalMap.put("총 수입 금액", formatWon(totalIncome));
    totalMap.put("총 지출 금액", formatWon(totalExpense));

    return totalMap;
  }



  private Map<String, String> getThisCategoryAmount(List<TransactionEntity> thisTList, List<String> categories, AnalyzeType type) {
    Map<String, String> map = new HashMap<>();

    if (thisTList.isEmpty()) {
      for(String category : categories) {
        map.put(category, "0원");
      }

      return map;
    }


    for (TransactionEntity t : thisTList) {
      String categoryName = t.getCategory().getCategoryName();
      map.put(categoryName, type.equals(AnalyzeType.INCOME)
          ? formatWon(t.getTotalIncomeAmount()) : formatWon(t.getTotalExpensesAmount()));
    }

    for(String category : categories) {
      if (!map.containsKey(category)) {
        map.put(category, "0원");
      }
    }

    return map;
  }

  private Map<String, String> getLastCategoryAmount(List<TransactionEntity> lastTList, List<String> categories, AnalyzeType type) {
    Map<String, String> map = new HashMap<>();

    if (lastTList.isEmpty()) {
      for(String category : categories) {
        map.put(category, "0원");
      }

      return map;
    }

    Map<String, Long> categoryMap = new HashMap<>();

    for (TransactionEntity t : lastTList) {
      String categoryName = t.getCategory().getCategoryName();
      if (type.equals(AnalyzeType.LAST_INCOME)) {
        categoryMap.put(categoryName,
            categoryMap.getOrDefault(categoryName, 0L) + t.getTotalIncomeAmount());
      } else {
        categoryMap.put(categoryName,
            categoryMap.getOrDefault(categoryName, 0L) + t.getTotalExpensesAmount());
      }
    }

    for(String category : categories) {
      if (!categoryMap.containsKey(category)) {
        categoryMap.put(category, 0L);
      }
    }

    for(Map.Entry<String, Long> entry : categoryMap.entrySet()) {
      map.put(entry.getKey(), formatWon(entry.getValue() / 3));
    }

    return map;
  }
}
