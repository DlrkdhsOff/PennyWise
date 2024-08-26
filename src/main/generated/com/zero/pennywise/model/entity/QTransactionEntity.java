package com.zero.pennywise.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTransactionEntity is a Querydsl query type for TransactionEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransactionEntity extends EntityPathBase<TransactionEntity> {

    private static final long serialVersionUID = -1494184870L;

    public static final QTransactionEntity transactionEntity = new QTransactionEntity("transactionEntity");

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final BooleanPath isFixed = createBoolean("isFixed");

    public final NumberPath<Long> transactionId = createNumber("transactionId", Long.class);

    public final StringPath type = createString("type");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QTransactionEntity(String variable) {
        super(TransactionEntity.class, forVariable(variable));
    }

    public QTransactionEntity(Path<? extends TransactionEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTransactionEntity(PathMetadata metadata) {
        super(TransactionEntity.class, metadata);
    }

}

