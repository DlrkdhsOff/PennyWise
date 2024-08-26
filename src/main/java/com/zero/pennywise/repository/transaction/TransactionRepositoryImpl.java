package com.zero.pennywise.repository.transaction;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.model.entity.QCategoriesEntity;
import com.zero.pennywise.model.entity.QTransactionEntity;
import com.zero.pennywise.model.response.TransactionsDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionQueryRepository {

  public final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<TransactionsDTO> getAllTransaction(Long userId, String page) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    int pageSize = 10;
    int offset = (Integer.parseInt(page) - 1) * pageSize;

    return jpaQueryFactory
        .select(Projections.fields(TransactionsDTO.class,
            t.type.as("type"),
            c.categoryName.as("categoryName"),
            t.amount.as("amount"),
            t.description.as("description"),
            t.dateTime.as("dateTime")))
        .from(t)
        .join(c).on(t.categoryId.eq(c.categoryId))
        .where(t.userId.eq(userId))
        .limit(pageSize)
        .offset(offset)
        .fetch();
  }

  @Override
  public List<TransactionsDTO> getTransactionsByCategory(Long userId, String categoryName, String page) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    int pageSize = 10;
    int offset = (Integer.parseInt(page) - 1) * pageSize;

    return jpaQueryFactory
        .select(Projections.fields(TransactionsDTO.class,
            t.type.as("type"),
            c.categoryName.as("categoryName"),
            t.amount.as("amount"),
            t.description.as("description"),
            t.dateTime.as("dateTime")))
        .from(t)
        .join(c).on(t.categoryId.eq(c.categoryId), c.categoryName.eq(categoryName))
        .where(t.userId.eq(userId))
        .limit(pageSize)
        .offset(offset)
        .fetch();
  }
}