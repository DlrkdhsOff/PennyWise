package com.zero.pennywise.repository.budget;

import static com.zero.pennywise.model.entity.QBudgetEntity.budgetEntity;
import static com.zero.pennywise.model.entity.QCategoriesEntity.categoriesEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.model.entity.QBudgetEntity;
import com.zero.pennywise.model.entity.QCategoriesEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BudgetRepositoryImpl implements BudgetQueryRepository {

  public final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<String> getAllCategory(Long userId, String page) {
    QCategoriesEntity c = categoriesEntity;
    QBudgetEntity b = budgetEntity;

    int pageSize = 10;
    int offset = (Integer.parseInt(page) - 1) * pageSize;

    return jpaQueryFactory
        .select(c.categoryName)
        .from(b)
        .join(c).on(b.categoryId.eq(c.categoryId))
        .where(b.userId.eq(userId))
        .limit(pageSize)
        .offset(offset)
        .fetch();
  }

}
