package com.zero.pennywise.repository.querydsl.category;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zero.pennywise.entity.QCategoryEntity;
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
    QCategoryEntity c = QCategoryEntity.categoryEntity;

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
    QCategoryEntity c = QCategoryEntity.categoryEntity;

    return jpaQueryFactory
        .select(c.count())
        .from(c)
        .where(c.user.id.eq(userId))
        .fetchOne();
  }
}
