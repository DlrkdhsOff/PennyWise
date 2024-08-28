package com.zero.pennywise.repository.querydsl;

import static com.zero.pennywise.model.entity.QBudgetEntity.budgetEntity;
import static com.zero.pennywise.model.entity.QCategoriesEntity.categoriesEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.model.entity.QBudgetEntity;
import com.zero.pennywise.model.entity.QCategoriesEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryQueryRepository {

  public final JPAQueryFactory jpaQueryFactory;


  // 총 데이터의 개수
  private Long getBudgetCount(QBudgetEntity b, QCategoriesEntity c, Long userId) {
    return jpaQueryFactory
        .select(b.count())
        .from(b)
        .join(c).on(b.categoryId.eq(c.categoryId))
        .where(b.userId.eq(userId))
        .fetchOne();
  }

  @Override
  public Page<String> getAllCategory(Long userId, Pageable page) {
    QCategoriesEntity c = categoriesEntity;
    QBudgetEntity b = budgetEntity;

    List<String> list = jpaQueryFactory
        .select(c.categoryName)
        .from(b)
        .join(c).on(b.categoryId.eq(c.categoryId))
        .where(b.userId.eq(userId))
        .limit(page.getPageSize())
        .offset(page.getOffset())
        .fetch();

    Long total = getBudgetCount(b, c, userId);
    return new PageImpl<>(list, page, total);
  }
}
