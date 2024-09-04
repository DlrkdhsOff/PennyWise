package com.zero.pennywise.repository.querydsl.transaction;

import com.zero.pennywise.model.request.transaction.CategoryAmountDTO;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.response.TransactionsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionQueryRepository {

  Page<TransactionsDTO> getAllTransaction(UserEntity user, Pageable page);

  Page<TransactionsDTO> getTransactionsByCategory(UserEntity user, String categoryName, Pageable page);

  CategoryAmountDTO getTotalAmountByUserIdAndCategoryId(Long userId, Long categoryId, String thisMonth);

  void updateCategory(Long useId, Long categoryId, Long newCategoryId);
}
