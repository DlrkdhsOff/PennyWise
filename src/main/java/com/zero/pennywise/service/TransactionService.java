package com.zero.pennywise.service;

import com.zero.pennywise.model.dto.TransactionDTO;
import com.zero.pennywise.model.response.Response;
import com.zero.pennywise.model.response.TransactionsDTO;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.transaction.TransactionRepository;
import com.zero.pennywise.status.BudgetTrackerStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final CategoriesRepository categoriesRepository;
  private final TransactionRepository transactionRepository;

  // 수입/지출 등록
  public Response transaction(Long userId, TransactionDTO transactionDTO) {
    return categoriesRepository.findByCategoryName(transactionDTO.getCategoryName())
        .map(category -> {
          transactionRepository.save(
              TransactionDTO.of(userId, category.getCategoryId(), transactionDTO)
          );

          return new Response(BudgetTrackerStatus.SUCCESS_TRANSACTION_REGISTRATION);
        })

        // 사용자가 등록한 카테고리가 아닐 경우
        .orElse(new Response(BudgetTrackerStatus.CATEGORY_NOT_FOUND));
  }

  // 수입 / 지출 내역
  public Object getTransactionList(Long userId, String categoryName, String page) {
    // 전체 거래 내역 / 카테고리별 거래 내역
    List<TransactionsDTO> transactions = (StringUtils.hasText(categoryName))
        ? transactionRepository.getTransactionsByCategory(userId, categoryName, page)
        : transactionRepository.getAllTransaction(userId, page);


    if (transactions.isEmpty()) {
      return new Response(StringUtils.hasText(categoryName)
          ? BudgetTrackerStatus.CATEGORY_NOT_FOUND
          : BudgetTrackerStatus.TRANSACTIONS_NOT_FOUND);
    }
    return transactions;
  }
}
