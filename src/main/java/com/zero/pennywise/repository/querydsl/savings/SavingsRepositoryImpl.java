//package com.zero.pennywise.repository.querydsl.savings;
//
//import com.querydsl.core.types.Expression;
//import com.querydsl.core.types.Projections;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.zero.pennywise.entity.QSavingsEntity;
//import com.zero.pennywise.entity.QTransactionEntity;
//import com.zero.pennywise.model.response.savings.SavingsDataDTO;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//@Repository
//@RequiredArgsConstructor
//public class SavingsRepositoryImpl implements SavingsQueryRepository{
//
//  private final JPAQueryFactory jpaQueryFactory;
//
//  @Override
//  public Page<SavingsDataDTO> getAllSavings(Long userId, Pageable page, Long categoryId) {
//    QSavingsEntity s = QSavingsEntity.savingsEntity;
//    QTransactionEntity t = QTransactionEntity.transactionEntity;
//
//    List<SavingsDataDTO> savingsData = jpaQueryFactory
//        .select(getExpression(userId, categoryId))
//        .from(s)
//        .where(s.user.id.eq(userId))
//        .limit(page.getPageSize())
//        .offset(page.getOffset())
//        .fetch();
//
//    Long total = getTotalCount(userId);
//
//    return new PageImpl<>(savingsData, page, total);
//  }
//
//  private Long getTotalCount(Long userId) {
//    QSavingsEntity s = QSavingsEntity.savingsEntity;
//    return jpaQueryFactory
//        .select(s.count())
//        .from(s)
//        .where(s.user.id.eq(userId))
//        .fetchOne();
//  }
//
//  private Expression<SavingsDataDTO> getExpression(Long userId, Long categoryId) {
//    QTransactionEntity t = QTransactionEntity.transactionEntity;
//    QSavingsEntity s = QSavingsEntity.savingsEntity;
//
//    return Projections.constructor(SavingsDataDTO.class,
//        s.name,
//        s.amount,
//        jpaQueryFactory.select(t.amount.sum().coalesce(0L))
//            .from(t)
//            .where(t.user.id.eq(userId)
//                .and(t.categoryId.eq(categoryId))
//                .and(t.description.eq(s.name.concat(s.description)))),
//        s.description,
//        s.startDate,
//        s.endDate,
//        s.createAt
//    );
//  }
//}