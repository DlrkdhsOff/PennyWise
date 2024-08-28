package com.zero.pennywise.repository.querydsl;

import static com.zero.pennywise.status.TransactionStatus.FIXED_EXPENSES;
import static com.zero.pennywise.status.TransactionStatus.FIXED_INCOME;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.model.entity.QCategoriesEntity;
import com.zero.pennywise.model.entity.QTransactionEntity;
import com.zero.pennywise.model.response.TransactionsDTO;
import com.zero.pennywise.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransactionRepositoryImpl implements TransactionQueryRepository {

  public final JPAQueryFactory jpaQueryFactory;
  private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);


  // 전체 거래 내역
  @Override
  public Page<TransactionsDTO> getAllTransaction(Long userId, Pageable page) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    List<TransactionsDTO> list =  jpaQueryFactory
        .select(selectTransactionAndCategoryColumn(t, c))
        .from(t)
        .join(c).on(t.categoryId.eq(c.categoryId))
        .where(t.userId.eq(userId))
        .limit(page.getPageSize())
        .offset(page.getOffset())
        .fetch();

    Long total = getTransactionCount(t, c, userId, null);
    return new PageImpl<>(list, page, total);
  }

  // 카테고리별 거래 내역
  @Override
  public Page<TransactionsDTO> getTransactionsByCategory(Long userId, String categoryName, Pageable page) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    List<TransactionsDTO> list = jpaQueryFactory
        .select(selectTransactionAndCategoryColumn(t, c))
        .from(t)
        .join(c).on(t.categoryId.eq(c.categoryId), c.categoryName.eq(categoryName))
        .where(t.userId.eq(userId))
        .limit(page.getPageSize())
        .offset(page.getOffset())
        .fetch();

    Long total = getTransactionCount(t, c, userId, categoryName);
    return new PageImpl<>(list, page, total);
  }

  // 총 데이터의 개수
  private Long getTransactionCount(QTransactionEntity t, QCategoriesEntity c, Long userId, String categoryName) {

    if (categoryName == null) {
      return jpaQueryFactory
          .select(t.count())
          .from(t)
          .join(c).on(t.categoryId.eq(c.categoryId))
          .where(t.userId.eq(userId))
          .fetchOne();
    } else {
      return jpaQueryFactory
          .select(t.count())
          .from(t)
          .join(c).on(t.categoryId.eq(c.categoryId), c.categoryName.eq(categoryName))
          .where(t.userId.eq(userId))
          .fetchOne();
    }
  }


  // 중복 코드 메서드로 추출(사용할 컬럼)
  private Expression<TransactionsDTO> selectTransactionAndCategoryColumn(QTransactionEntity t, QCategoriesEntity c) {
    return Projections.fields(TransactionsDTO.class,
        t.type.stringValue().as("type"),
        c.categoryName.as("categoryName"),
        t.amount.as("amount"),
        t.description.as("description"),
        t.dateTime.as("dateTime"));
  }

  @Transactional
  @Override
  public void updateFixedTransaction(String lastMonthsDate, String today) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;

    long affectedRows = jpaQueryFactory
        .update(t)
        .set(t.fixedDateTime, today)
        .where(t.type.eq(FIXED_EXPENSES)
            .or(t.type.eq(FIXED_INCOME))
            .and(t.dateTime.startsWith(lastMonthsDate)))
        .execute();

    logger.info("날짜 : {} {}개 행이 수정 되었습니다. ",today, affectedRows);
  }


}