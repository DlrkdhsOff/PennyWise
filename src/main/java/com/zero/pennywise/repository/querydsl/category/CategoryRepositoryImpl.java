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
}
