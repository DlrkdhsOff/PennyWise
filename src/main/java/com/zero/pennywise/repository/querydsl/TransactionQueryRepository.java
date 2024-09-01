package com.zero.pennywise.repository.querydsl;

import com.zero.pennywise.model.dto.transaction.CategoryAmountDTO;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.model.entity.UserEntity;
import com.zero.pennywise.model.response.TransactionsDTO;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionQueryRepository {

  Page<TransactionsDTO> getAllTransaction(UserEntity user, Pageable page);

  Page<TransactionsDTO> getTransactionsByCategory(UserEntity user, String categoryName, Pageable page);

  Map<String, Long> getTotalAmount(Long userId, String thisMonth);

  CategoryAmountDTO getTotalAmountByUserIdAndCategoryId(Long userId, Long categoryId, String thisMonth);

  void updateCategoryId(Long useId, Long categoryId, CategoriesEntity updatedCategory);
}
