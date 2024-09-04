package com.zero.pennywise.repository.querydsl.category;


import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.QCategoriesEntity;
import com.zero.pennywise.entity.QTransactionEntity;
import com.zero.pennywise.entity.QUserCategoryEntity;
import com.zero.pennywise.model.response.Categories;
import com.zero.pennywise.model.response.TransactionsDTO;
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

  @Override
  public List<Categories> getAllCategory(Long userId, Pageable page) {
    QUserCategoryEntity uC = QUserCategoryEntity.userCategoryEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    return jpaQueryFactory
        .select(selectCategoryIdAndCategoryName())
        .from(c)
        .join(uC).on(c.categoryId.eq(uC.category.categoryId))
        .where(uC.user.id.eq(userId))
        .fetch();

  }

  // 선택할 컬럼 추출
  private Expression<Categories> selectCategoryIdAndCategoryName() {
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    return Projections.fields(Categories.class,
        c.categoryId.as("categoryId"),
        c.categoryName.as("categoryName"));
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
