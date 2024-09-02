package com.zero.pennywise.repository.querydsl.transaction;


import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.QCategoriesEntity;
import com.zero.pennywise.entity.QTransactionEntity;
import com.zero.pennywise.model.dto.transaction.CategoryAmountDTO;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.TransactionsDTO;
import com.zero.pennywise.service.TransactionService;
import com.zero.pennywise.status.TransactionStatus;
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

  public final JPAQueryFactory jpaQueryFactory;
  private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);


  // 전체 거래 내역
  @Override
  public Page<TransactionsDTO> getAllTransaction(UserEntity user, Pageable page) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    List<TransactionsDTO> list =  jpaQueryFactory
        .select(selectTransactionAndCategoryColumn())
        .from(t)
        .join(c).on(t.categoryId.eq(c.categoryId))
        .where(t.user.id.eq(user.getId()))
        .limit(page.getPageSize())
        .offset(page.getOffset())
        .fetch();

    Long total = getTransactionCount(user, null);
    return new PageImpl<>(list, page, total);
  }

  // 카테고리별 거래 내역
  @Override
  public Page<TransactionsDTO> getTransactionsByCategory(UserEntity user, String categoryName, Pageable page) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    List<TransactionsDTO> list = jpaQueryFactory
        .select(selectTransactionAndCategoryColumn())
        .from(t)
        .join(c).on(t.categoryId.eq(c.categoryId), c.categoryName.eq(categoryName))
        .where(t.user.id.eq(user.getId()))
        .limit(page.getPageSize())
        .offset(page.getOffset())
        .fetch();

    Long total = getTransactionCount(user, categoryName);
    return new PageImpl<>(list, page, total);
  }

  // 총 데이터의 개수
  private Long getTransactionCount(UserEntity user, String categoryName) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    if (categoryName == null) {
      return jpaQueryFactory
          .select(t.count())
          .from(t)
          .join(c).on(t.categoryId.eq(c.categoryId))
          .where(t.user.id.eq(user.getId()))
          .fetchOne();
    } else {
      return jpaQueryFactory
          .select(t.count())
          .from(t)
          .join(c).on(t.categoryId.eq(c.categoryId), c.categoryName.eq(categoryName))
          .where(t.user.id.eq(user.getId()))
          .fetchOne();
    }
  }


  // 중복 코드 메서드로 추출(사용할 컬럼)
  private Expression<TransactionsDTO> selectTransactionAndCategoryColumn() {
    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    return Projections.fields(TransactionsDTO.class,
        t.transactionId.as("transactionId"),
        t.type.stringValue().as("type"),
        c.categoryName.as("categoryName"),
        t.amount.as("amount"),
        t.description.as("description"),
        t.dateTime.as("dateTime"));
  }


  // 카테고리 변경시 해당 변경된 categoryId로 변경
  @Override
  public void updateCategoryId(Long userId, Long categoryId, CategoriesEntity updatedCategory) {
    QTransactionEntity t = QTransactionEntity.transactionEntity;

    jpaQueryFactory
        .update(t)
        .set(t.categoryId, updatedCategory.getCategoryId())
        .where(
            t.user.id.eq(userId),
            t.categoryId.eq(categoryId)
        )
        .execute();
  }

  @Override
  public CategoryAmountDTO getTotalAmountByUserIdAndCategoryId(Long userId, Long categoryId,
      String thisMonth) {

    QTransactionEntity t = QTransactionEntity.transactionEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    String categoryName = jpaQueryFactory
        .select(c.categoryName)
        .from(c)
        .where(c.categoryId.eq(categoryId))
        .fetchOne();

    Long totalExpenses = getAmount(userId, categoryId, thisMonth,
        TransactionStatus.EXPENSES, TransactionStatus.FIXED_EXPENSES);

    Long totalIncome = getAmount(userId, categoryId, thisMonth,
        TransactionStatus.INCOME, TransactionStatus.FIXED_INCOME);

    totalExpenses = (totalExpenses == null) ? 0 : totalExpenses;
    totalIncome = (totalIncome == null) ? 0 : totalIncome;

    return new CategoryAmountDTO(categoryName, totalIncome, totalExpenses);
  }


  // 공통 메서드 : 해당 값과 일치하는 데이터의 합계
  private Long getAmount(Long userId, Long categoryId, String thisMonths,
      TransactionStatus notFixed, TransactionStatus fixed) {

    QTransactionEntity t = QTransactionEntity.transactionEntity;

    return jpaQueryFactory
        .select(t.amount.sum())
        .from(t)
        .where(
            t.user.id.eq(userId),
            t.dateTime.startsWith(thisMonths),
            t.type.eq(fixed)
                .or( t.type.eq(notFixed)),
            categoryId != null ? t.categoryId.eq(categoryId) : null
        )
        .fetchOne();
  }


}