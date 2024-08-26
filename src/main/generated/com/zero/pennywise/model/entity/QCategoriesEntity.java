package com.zero.pennywise.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCategoriesEntity is a Querydsl query type for CategoriesEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategoriesEntity extends EntityPathBase<CategoriesEntity> {

    private static final long serialVersionUID = -883710458L;

    public static final QCategoriesEntity categoriesEntity = new QCategoriesEntity("categoriesEntity");

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final StringPath categoryName = createString("categoryName");

    public final BooleanPath shared = createBoolean("shared");

    public QCategoriesEntity(String variable) {
        super(CategoriesEntity.class, forVariable(variable));
    }

    public QCategoriesEntity(Path<? extends CategoriesEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategoriesEntity(PathMetadata metadata) {
        super(CategoriesEntity.class, metadata);
    }

}

