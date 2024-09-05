package com.zero.pennywise.repository.querydsl.category;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.QCategoriesEntity;
import com.zero.pennywise.entity.QUserCategoryEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryQueryRepository {

  public final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<CategoriesEntity> getAllCategory(Long userId) {
    QUserCategoryEntity uC = QUserCategoryEntity.userCategoryEntity;
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    return jpaQueryFactory
        .select(c)
        .from(c)
        .join(uC).on(c.categoryId.eq(uC.category.categoryId))
        .where(uC.user.id.eq(userId))
        .fetch();

  }

  @Override
  public void updateCategory(Long userId, Long categoryId, Long newCategoryId) {

    QUserCategoryEntity uC = QUserCategoryEntity.userCategoryEntity;

    jpaQueryFactory
        .update(uC)
        .set(uC.category.categoryId, newCategoryId)
        .where(
            uC.user.id.eq(userId),
            uC.category.categoryId.eq(categoryId)
        )
        .execute();
  }
}
