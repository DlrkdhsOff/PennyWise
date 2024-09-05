package com.zero.pennywise.repository.querydsl.budget;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.QBudgetEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class BudgetRepositoryImpl implements BudgetQueryRepository {

  public final JPAQueryFactory jpaQueryFactory;

  @Override
  @Transactional
  public void updateCategory(Long userId, Long categoryId, Long newCategoryId) {
    QBudgetEntity b = QBudgetEntity.budgetEntity;

    jpaQueryFactory
        .update(b)
        .set(b.category.categoryId, newCategoryId)
        .where(
            b.user.id.eq(userId),
            b.category.categoryId.eq(categoryId)
        )
        .execute();
  }

}
