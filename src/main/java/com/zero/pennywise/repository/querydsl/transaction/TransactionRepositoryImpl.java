package com.zero.pennywise.repository.querydsl.transaction;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.entity.transaction.QTransactionEntity;
import com.zero.pennywise.enums.TransactionType;
import com.zero.pennywise.model.request.transaction.TransactionInfoDTO;
import com.zero.pennywise.model.response.page.PageResponse;
import com.zero.pennywise.model.response.transaction.Transactions;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public PageResponse<Transactions> getTransactionInfo(UserEntity user, TransactionInfoDTO transactionInfoDTO, int page) {
    QTransactionEntity transaction = QTransactionEntity.transactionEntity;
    BooleanBuilder builder = new BooleanBuilder();

    builder.and(transaction.user.eq(user));

    if (transactionInfoDTO.getCategoryName() != null) {
      builder.and(transaction.category.categoryName.eq(transactionInfoDTO.getCategoryName()));
    }
    if (transactionInfoDTO.getTransactionType() != null) {
      builder.and(transaction.type.eq(TransactionType.getTransactionType(transactionInfoDTO.getTransactionType())));
    }
    if (transactionInfoDTO.getMonth() != null) {
      builder.and(transaction.dateTime.between(LocalDateTime.now(), LocalDateTime.now().minusMonths(transactionInfoDTO.getMonth())));
    }

    List<Transactions> transactions = Transactions.of(
        jpaQueryFactory
        .selectFrom(transaction)
        .where(builder)
        .fetch());


    return PageResponse.of(transactions, page);
  }
}