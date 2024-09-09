package com.zero.pennywise.repository.querydsl.transaction;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.QCategoriesEntity;
import com.zero.pennywise.entity.QTransactionEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.transaction.TransactionsDTO;
import com.zero.pennywise.status.TransactionStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransactionRepositoryImpl implements TransactionQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  // 전체 거래 내역 조회
  @Override
  public Page<TransactionsDTO> getAllTransaction(UserEntity user, String categoryName, Pageable page) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    List<TransactionsDTO> list = jpaQueryFactory
        .select(selectTransactionAndCategoryColumn())
        .from(t)
        .join(c).on(t.categoryId.eq(c.categoryId))
        .where(
            t.user.id.eq(user.getId()),
            categoryName != null ? c.categoryName.eq(categoryName) : null
        )
        .limit(page.getPageSize())
        .offset(page.getOffset())
        .fetch();


    Long total = getTransactionCount(user, categoryName);
    return new PageImpl<>(list, page, total);
  }

  // 총 데이터 개수 조회
  private Long getTransactionCount(UserEntity user, String categoryName) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    return jpaQueryFactory
        .select(t.count())
        .from(t)
        .join(c).on(t.categoryId.eq(c.categoryId))
        .where(
            t.user.id.eq(user.getId()),
            categoryName != null ? c.categoryName.eq(categoryName) : null
        )
        .fetchOne();
  }

  // 선택할 컬럼 추출
  private Expression<TransactionsDTO> selectTransactionAndCategoryColumn() {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    return Projections.fields(TransactionsDTO.class,
        t.transactionId.as("transactionId"),
        t.type.stringValue().as("type"),
        c.categoryName.as("categoryName"),
        t.amount.as("amount"),
        t.description.as("description"),
        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", t.dateTime).as("dateTime"));
  }


  @Override
  public List<TransactionEntity> findByLastMonthTransaction(String lastMonthsDate) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;

    return jpaQueryFactory
        .selectFrom(t)
        .where(
            t.dateTime.stringValue().startsWith(lastMonthsDate)
        )
        .fetch();
  }

  @Override
  public Long getExpenses(Long userId, Long categoryId, String thisMonths) {

    QTransactionEntity t = QTransactionEntity.transactionEntity;

    Long totalExpense =  jpaQueryFactory
        .select(t.amount.sum())
        .from(t)
        .where(
            t.user.id.eq(userId),
            t.dateTime.stringValue().startsWith(thisMonths),
            t.type.eq(TransactionStatus.EXPENSES)
                .or(t.type.eq(TransactionStatus.FIXED_EXPENSES)),
            categoryId != null ? t.categoryId.eq(categoryId) : null
        )
        .fetchOne();

    return (totalExpense != null) ? totalExpense : 0L;
  }

  @Override
  public Long getTracsactionAvgLastThreeMonth(Long userId, Long categoryId, LocalDateTime startDateTime,
      LocalDateTime endDateTime) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;

    Long expensesData = jpaQueryFactory
        .select((t.amount.sum()))
        .from(t)
        .where(
            t.user.id.eq(userId),
            t.type.eq(TransactionStatus.EXPENSES)
                .or(t.type.eq(TransactionStatus.FIXED_EXPENSES)),
            t.dateTime.between(startDateTime, endDateTime),
            categoryId != null ? t.categoryId.eq(categoryId) : null
        )
        .fetchOne();
    return (expensesData != null) ? expensesData / 3 : 0L;
  }
}