package com.zero.pennywise.repository.querydsl.category;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.QCategoriesEntity;
import com.zero.pennywise.entity.QUserCategoryEntity;
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
  private Long getBudgetCount(QUserCategoryEntity uC, Long userId) {
    return jpaQueryFactory
        .select(uC.count())
        .from(uC)
        .where(uC.user.id.eq(userId))
        .fetchOne();
  }

  @Override
  public Page<String> getAllCategory(Long userId, Pageable page) {
    QUserCategoryEntity uC = QUserCategoryEntity.userCategoryEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    List<String> list = jpaQueryFactory
        .select(c.categoryName)
        .from(c)
        .join(uC).on(c.categoryId.eq(uC.category.categoryId))
        .where(uC.user.id.eq(userId))
        .limit(page.getPageSize())
        .offset(page.getOffset())
        .fetch();

    Long total = getBudgetCount(uC, userId);
    return new PageImpl<>(list, page, total);
  }

  @Override
  public void updateCategory(Long userId, Long categoryId, CategoriesEntity newCategory) {

    QUserCategoryEntity uC = QUserCategoryEntity.userCategoryEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    jpaQueryFactory
        .update(uC)
        .set(uC.category.categoryId, newCategory.getCategoryId())
        .where(
            uC.user.id.eq(userId),
            uC.category.categoryId.eq(categoryId)
        )
        .execute();
  }
}
