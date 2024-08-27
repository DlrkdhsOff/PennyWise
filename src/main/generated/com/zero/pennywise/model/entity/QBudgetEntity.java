package com.zero.pennywise.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBudgetEntity is a Querydsl query type for BudgetEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBudgetEntity extends EntityPathBase<BudgetEntity> {

    private static final long serialVersionUID = -700950545L;

    public static final QBudgetEntity budgetEntity = new QBudgetEntity("budgetEntity");

    public final QDateEntity _super = new QDateEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> budgetId = createNumber("budgetId", Long.class);

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    //inherited
    public final DatePath<java.time.LocalDate> createAt = _super.createAt;

    //inherited
    public final DatePath<java.time.LocalDate> updateAt = _super.updateAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QBudgetEntity(String variable) {
        super(BudgetEntity.class, forVariable(variable));
    }

    public QBudgetEntity(Path<? extends BudgetEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBudgetEntity(PathMetadata metadata) {
        super(BudgetEntity.class, metadata);
    }

}

