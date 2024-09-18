package com.zero.pennywise.repository.querydsl.transaction;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.QCategoryEntity;
import com.zero.pennywise.entity.QTransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.enums.TransactionStatus;
import com.zero.pennywise.model.response.transaction.TransactionsDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  // 전체 거래 내역 조회
  @Override
  public Page<TransactionsDTO> getAllTransaction(UserEntity user, String categoryName, Pageable page) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoryEntity c = QCategoryEntity.categoryEntity;

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
    QCategoryEntity c = QCategoryEntity.categoryEntity;

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
    QCategoryEntity c = QCategoryEntity.categoryEntity;

    return Projections.fields(TransactionsDTO.class,
        t.transactionId.as("transactionId"),
        t.type.stringValue().as("type"),
        c.categoryName.as("categoryName"),
        t.amount.as("amount"),
        t.description.as("description"),
        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s')", t.dateTime).as("dateTime"));
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
  public Long getCurrentAmount(UserEntity user, Long categoryId, String description) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;

    Long currentAmount = jpaQueryFactory
        .select(t.amount.sum())
        .from(t)
        .where(
            t.user.id.eq(user.getId()),
            t.categoryId.eq(categoryId),
            t.description.eq(description)
        )
        .fetchOne();


    return currentAmount == null ? 0L : currentAmount;
  }

  @Override
  public void endSavings(Long userId, Long categoryId, String description) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;

    jpaQueryFactory
        .update(t)
        .set(t.type, TransactionStatus.END)
        .where(
            t.user.id.eq(userId),
            t.categoryId.eq(categoryId),
            t.description.eq(description)
        )
        .execute();
  }

  @Override
  public Long getExpensesAvgLastThreeMonth(Long userId, Long categoryId, LocalDateTime startDateTime,
      LocalDateTime endDateTime) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;

    Long totalExpenses = jpaQueryFactory
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
    return (totalExpenses != null) ? totalExpenses / 3 : 0L;
  }


  @Override
  public Long getIncomeAvgLastThreeMonth(Long userId, LocalDateTime startDateTime,
      LocalDateTime endDateTime) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;

    Long totalIncome = jpaQueryFactory
        .select((t.amount.sum()))
        .from(t)
        .where(
            t.user.id.eq(userId),
            t.type.eq(TransactionStatus.FIXED_INCOME),
            t.dateTime.between(startDateTime, endDateTime)
        )
        .fetchOne();
    return (totalIncome != null) ? totalIncome / 3 : 0L;
  }

}