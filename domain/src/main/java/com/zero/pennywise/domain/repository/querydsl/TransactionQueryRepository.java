package com.zero.pennywise.domain.repository.querydsl;

import static java.util.Optional.ofNullable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.domain.entity.QTransactionEntity;
import com.zero.pennywise.domain.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.domain.model.response.transaction.TotalAmount;
import com.zero.pennywise.domain.entity.CategoryEntity;
import com.zero.pennywise.domain.entity.TransactionEntity;
import com.zero.pennywise.domain.entity.UserEntity;
import com.zero.pennywise.domain.model.response.transaction.Transactions;
import com.zero.pennywise.domain.model.type.AnalyzeType;
import com.zero.pennywise.domain.model.type.TransactionType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
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

    if (transactionInfoDTO.getCategoryName() != null && !transactionInfoDTO.getCategoryName().isEmpty()) {
      log.info("카테고리  {}",transactionInfoDTO.getCategoryName());
      builder.and(qTransaction.category.categoryName.eq(transactionInfoDTO.getCategoryName()));
    }
    if (transactionInfoDTO.getTransactionType() != null
        && !transactionInfoDTO.getTransactionType().isEmpty()
        && !transactionInfoDTO.getTransactionType().equals("전체")) {
      log.info("거래 타입 {}", transactionInfoDTO.getTransactionType());
      builder.and(qTransaction.type.eq(TransactionType.getTransactionType(transactionInfoDTO.getTransactionType())));
    }
    if (transactionInfoDTO.getMonth() != null) {
      log.info("달 {}",transactionInfoDTO.getMonth());
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

  public List<TransactionEntity> findLastTransaction(UserEntity user, AnalyzeType type) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(qTransaction.user.eq(user));

    if (type.equals(AnalyzeType.THIS)) {
      builder.and(qTransaction.createAt.between(
          LocalDateTime.now().withDayOfMonth(1),
          LocalDateTime.now()));
    } else {
      builder.and(qTransaction.createAt.between(
          LocalDateTime.now().minusMonths(4),
          LocalDateTime.now().minusMonths(1)));
    }

    return jpaQueryFactory
        .selectFrom(qTransaction)
        .where(builder)
        .orderBy(qTransaction.createAt.desc())
        .fetch();
  }


}