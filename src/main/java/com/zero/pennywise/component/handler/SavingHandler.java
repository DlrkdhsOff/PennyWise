package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.response.balances.Balances;
import com.zero.pennywise.model.response.savings.RecommendSavings;
import com.zero.pennywise.model.type.FailedResultCode;
import com.zero.pennywise.model.type.SavingsMessage;
import com.zero.pennywise.repository.querydsl.balance.BalanceQueryRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavingHandler {

  private final BalanceQueryRepository balanceQueryRepository;

  public List<Balances> getRecentAvg(UserEntity user) {
    List<Balances> balances = balanceQueryRepository.getRecentAvg(user);

    if(balances == null) {
      throw new GlobalException(FailedResultCode.TRANSACTION_NOT_FOUND);
    }

    return balances;
  }

  public RecommendSavings recommend(List<Balances> recentAvgList) {
    long incomeAvg = recentAvgList.stream()
        .mapToLong(Balances::getTotalIncomeAmount)
        .sum();

    if(incomeAvg < 500_000) {
      throw new GlobalException(FailedResultCode.AVERAGE_INCOME_TOO_LOW);
    }

    long expensesAvg = recentAvgList.stream()
        .mapToLong(Balances::getTotalExpensesAmount)
        .sum();

    long recommendAmount = (long) (incomeAvg * 0.2);
    if (incomeAvg * 0.8 >= expensesAvg) {
      return new RecommendSavings(
          SavingsMessage.RECOMMENDED_SAVINGS.getMessage(recommendAmount), recentAvgList);
    } else {
      recentAvgList = recentAvgList.stream()
          .sorted(Comparator.comparing(Balances::getTotalExpensesAmount))
          .toList();

      return new RecommendSavings(
          SavingsMessage.HIGH_SPENDING_WARNING.getMessage(recommendAmount, recentAvgList.get(0).getCategoryName()),
          recentAvgList);
    }
  }
}
