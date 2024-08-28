package com.zero.pennywise.repository.querydsl;

import static com.zero.pennywise.utils.PageUtils.calculateOffset;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.entity.QCategoriesEntity;
import com.zero.pennywise.model.entity.QTransactionEntity;
import com.zero.pennywise.model.response.TransactionsDTO;
import com.zero.pennywise.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransactionRepositoryImpl implements TransactionQueryRepository {

  public final JPAQueryFactory jpaQueryFactory;
  private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);


  // 전체 거래 내역
  @Override
  public List<TransactionsDTO> getAllTransaction(Long userId, String page) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    long pageSize = 10L;
    Long totalCount = getTransactionCount(t, c, userId, null);
    long[] data = calculateOffset(page, pageSize, totalCount);

    if (data[1] == -1) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "총 페이지 수는 " + data[0] + "개 입니다.");
    }

    return jpaQueryFactory
        .select(selectTransactionAndCategoryColumn(t, c))
        .from(t)
        .join(c).on(t.categoryId.eq(c.categoryId))
        .where(t.userId.eq(userId))
        .limit(pageSize)
        .offset(data[1])
        .fetch();
  }

  // 카테고리별 거래 내역
  @Override
  public List<TransactionsDTO> getTransactionsByCategory(Long userId, String categoryName, String page) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    long pageSize = 10L;
    Long totalCount = getTransactionCount(t, c, userId, categoryName);
    long[] data = calculateOffset(page, pageSize, totalCount);

    if (data[1] == -1) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "총 페이지 수는 " + data[0] + "개 입니다.");
    }

    return jpaQueryFactory
        .select(selectTransactionAndCategoryColumn(t, c))
        .from(t)
        .join(c).on(t.categoryId.eq(c.categoryId), c.categoryName.eq(categoryName))
        .where(t.userId.eq(userId))
        .limit(pageSize)
        .offset(data[1])
        .fetch();
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

}