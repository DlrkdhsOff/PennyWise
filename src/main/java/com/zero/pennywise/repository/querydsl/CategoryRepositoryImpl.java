package com.zero.pennywise.repository.querydsl;

import static com.zero.pennywise.model.entity.QBudgetEntity.budgetEntity;
import static com.zero.pennywise.model.entity.QCategoriesEntity.categoriesEntity;
import static com.zero.pennywise.utils.PageUtils.calculateOffset;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.entity.QBudgetEntity;
import com.zero.pennywise.model.entity.QCategoriesEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  public List<String> getAllCategory(Long userId, String page) {
    QCategoriesEntity c = categoriesEntity;
    QBudgetEntity b = budgetEntity;

    long pageSize = 10L;
    Long totalCount = getBudgetCount(b, c, userId);
    long[] data = calculateOffset(page, pageSize, totalCount);

    if (data[1] == -1) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "총 페이지 수는 " + data[0] + "개 입니다.");
    }

    return jpaQueryFactory
        .select(c.categoryName)
        .from(b)
        .join(c).on(b.categoryId.eq(c.categoryId))
        .where(b.userId.eq(userId))
        .limit(pageSize)
        .offset(data[1])
        .fetch();
  }
}
