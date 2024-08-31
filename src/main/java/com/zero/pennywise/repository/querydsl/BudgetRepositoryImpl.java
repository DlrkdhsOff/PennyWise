package com.zero.pennywise.repository.querydsl;

import static com.zero.pennywise.model.entity.QBudgetEntity.budgetEntity;
import static com.zero.pennywise.model.entity.QCategoriesEntity.categoriesEntity;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.model.dto.budget.BudgetDTO;
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
public class BudgetRepositoryImpl implements BudgetQueryRepository {

  public final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<BudgetDTO> findAllBudgetByUserId(Long userId, Pageable pageable) {
    QBudgetEntity b = budgetEntity;
    QCategoriesEntity c = categoriesEntity;

    List<BudgetDTO> list = jpaQueryFactory
        .select(selectBudgetAndCategory(c, b))
        .from(b)
        .join(c).on(b.category.categoryId.eq(c.categoryId))
        .where(b.user.id.eq(userId))
        .limit(pageable.getPageSize())
        .offset(pageable.getOffset())
        .fetch();

    Long total = countBudgetsByUserId(userId);  // 메서드 이름 변경
    return new PageImpl<>(list, pageable, total);
  }

  private Expression<BudgetDTO> selectBudgetAndCategory(QCategoriesEntity c, QBudgetEntity b) {
    return Projections.fields(BudgetDTO.class,
        c.categoryName.as("categoryName"),
        b.amount.as("amount"));
  }

  private Long countBudgetsByUserId(Long userId) {
    QBudgetEntity b = budgetEntity;

    return jpaQueryFactory
        .select(b.count())
        .from(b)
        .where(b.user.id.eq(userId))
        .fetchOne();
  }
}
