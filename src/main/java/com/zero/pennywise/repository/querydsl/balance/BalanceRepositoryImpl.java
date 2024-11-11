package com.zero.pennywise.repository.querydsl.balance;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.QBalanceEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.balances.Balances;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BalanceRepositoryImpl implements BalanceQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Balances> getRecentAvg(UserEntity user) {
    QBalanceEntity qBalance = QBalanceEntity.balanceEntity;

    String startDate = LocalDate.now().minusMonths(3)
        .format(DateTimeFormatter.ofPattern("yyyy-MM"));
    String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

    return jpaQueryFactory
        .select(
            Projections.constructor(
                Balances.class,
                qBalance.category.categoryName,
                qBalance.totalIncomeAmount.avg().longValue(),
                qBalance.totalExpensesAmount.avg().longValue()
            )
        )
        .from(qBalance)
        .where(
            qBalance.user.eq(user)
                .and(qBalance.recordMonth.between(startDate, endDate))
        )
        .groupBy(qBalance.category)
        .fetch();
  }
}