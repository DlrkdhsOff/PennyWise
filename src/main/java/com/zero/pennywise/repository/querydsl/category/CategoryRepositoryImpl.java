package com.zero.pennywise.repository.querydsl.category;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.QCategoriesEntity;
import com.zero.pennywise.entity.QUserCategoryEntity;
import com.zero.pennywise.entity.UserEntity;
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
  public Page<String> getAllCategory(Long userId, Pageable pageable) {
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    List<String> list = jpaQueryFactory
        .select(c.categoryName)
        .from(c)
        .where(c.user.id.eq(userId))
        .limit(pageable.getPageSize())
        .offset(pageable.getOffset())
        .fetch();

    Long total = getCategoryCount(userId);

    return new PageImpl<>(list, pageable, total);
  }


  private Long getCategoryCount(Long userId) {
    QCategoriesEntity c = QCategoriesEntity.categoriesEntity;

    return jpaQueryFactory
        .select(c.count())
        .from(c)
        .where(c.user.id.eq(userId))
        .fetchOne();
  }
}
