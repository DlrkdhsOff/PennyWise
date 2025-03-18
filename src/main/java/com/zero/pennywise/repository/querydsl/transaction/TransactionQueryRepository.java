package com.zero.pennywise.repository.querydsl.transaction;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.QTransactionEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.transaction.TransactionDTO;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.response.transaction.TotalAmount;
import com.zero.pennywise.model.type.TransactionType;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.response.transaction.Transactions;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionQueryRepository  {

  private final JPAQueryFactory jpaQueryFactory;
  QTransactionEntity qTransaction = QTransactionEntity.transactionEntity;


  public List<Transactions> getTransactionList(UserEntity user, TransactionInfoDTO transactionInfoDTO) {
    BooleanBuilder builder = buildConditions(user, transactionInfoDTO);

    List<Transactions> transactionList = Transactions.of(
        jpaQueryFactory
        .selectFrom(qTransaction)
        .where(builder)
        .fetch());

    return transactionList == null ? new ArrayList<>() : transactionList;

  }

  private BooleanBuilder buildConditions(UserEntity user, TransactionInfoDTO transactionInfoDTO) {
    BooleanBuilder builder = new BooleanBuilder();

    builder.and(qTransaction.user.eq(user));

    if (transactionInfoDTO.getCategoryName() != null) {
      builder.and(qTransaction.category.categoryName.eq(transactionInfoDTO.getCategoryName()));
    }
    if (transactionInfoDTO.getTransactionType() != null
        && !transactionInfoDTO.getTransactionType().isEmpty()
        && !transactionInfoDTO.getTransactionType().equals("전체")) {
      builder.and(qTransaction.type.eq(TransactionType.getTransactionType(transactionInfoDTO.getTransactionType())));
    }
    if (transactionInfoDTO.getMonth() != null) {
      builder.and(qTransaction.createAt.between(
          LocalDateTime.now().minusMonths(
              transactionInfoDTO.getMonth() == 0 ? 1 : transactionInfoDTO.getMonth()),
          LocalDateTime.now()));
    }

    return builder;
  }

  public TotalAmount getTotalAmount(UserEntity user, CategoryEntity category) {
    TransactionEntity transaction = jpaQueryFactory
        .selectFrom(qTransaction)
        .where(
            qTransaction.user.eq(user),
            qTransaction.category.eq(category),
            qTransaction.createAt.between(LocalDateTime.now().withDayOfMonth(1), LocalDateTime.now())
        )
        .orderBy(qTransaction.createAt.desc())
        .limit(1)
        .fetchOne();

    return new TotalAmount(
        transaction == null ? 0L : transaction.getTotalIncomeAmount(),
        transaction == null ? 0L : transaction.getTotalExpensesAmount()
    );
  }
}