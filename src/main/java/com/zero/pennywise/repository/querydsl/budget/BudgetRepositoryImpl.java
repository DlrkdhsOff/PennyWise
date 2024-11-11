package com.zero.pennywise.repository.querydsl.budget;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.QBalanceEntity;
import com.zero.pennywise.entity.QBudgetEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.budget.Budgets;
import com.zero.pennywise.model.response.page.PageResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BudgetRepositoryImpl implements BudgetQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public PageResponse<Budgets> getBudgetInfo(UserEntity user, String categoryName, int page) {
    QBudgetEntity budget = QBudgetEntity.budgetEntity;

    // 조건 빌더 설정
    BooleanBuilder condition = new BooleanBuilder();
    condition.and(budget.user.eq(user));

    if (categoryName != null) {
      condition.and(budget.category.categoryName.eq(categoryName));
    }

    // 예산 정보 조회
    List<BudgetEntity> budgetEntities = jpaQueryFactory
        .selectFrom(budget)
        .where(condition)
        .fetch();

    // 예산 정보와 지출 합계를 계산하여 Budgets 리스트 생성
    List<Budgets> budgetsList = budgetEntities.stream()
        .map(entity -> {
          Long totalExpenses = getTotalExpenses(user, entity.getCategory().getCategoryName());
          Long balance = entity.getAmount() - totalExpenses;
          return new Budgets(entity.getBudgetId(), entity.getCategory().getCategoryName(), entity.getAmount(), balance);
        })
        .collect(Collectors.toList());

    return PageResponse.of(budgetsList, page);
  }

  // 특정 사용자의 주어진 카테고리에 해당하는 이달의 지출 합계를 조회하는 메서드
  private Long getTotalExpenses(UserEntity user, String categoryName) {
    QBalanceEntity balance = QBalanceEntity.balanceEntity;

    // 현재 날짜로 월 단위 형식 생성
    String recordMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

    Long totalExpenses =  jpaQueryFactory
        .select(balance.totalExpensesAmount)
        .from(balance)
        .where(balance.user.eq(user),
            balance.category.categoryName.eq(categoryName),
            balance.recordMonth.eq(recordMonth))
        .fetchOne();

    return totalExpenses == null ? 0L : totalExpenses;
  }
}