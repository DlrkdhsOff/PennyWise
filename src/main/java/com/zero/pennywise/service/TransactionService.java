package com.zero.pennywise.service;

import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.TransactionDTO;
import com.zero.pennywise.model.response.TransactionPage;
import com.zero.pennywise.model.response.TransactionsDTO;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.TransactionQueryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final CategoriesRepository categoriesRepository;
  private final TransactionRepository transactionRepository;
  private final TransactionQueryRepository transactionQueryRepository;

  // 수입/지출 등록
  public String transaction(Long userId, TransactionDTO transactionDTO) {
    return categoriesRepository.findByCategoryName(transactionDTO.getCategoryName())
        .map(category -> {
          transactionRepository.save(
              TransactionDTO.of(userId, category.getCategoryId(), transactionDTO)
          );
          return "거래 등록 성공";
        })

        // 사용자가 등록한 카테고리가 아닐 경우
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다."));
  }

  // 수입 / 지출 내역
  public TransactionPage getTransactionList(Long userId, String categoryName, Pageable page) {
    Pageable pageable = page(page);

    TransactionPage transactions = (StringUtils.hasText(categoryName))
        ? TransactionsDTO.of(transactionQueryRepository.getTransactionsByCategory(userId, categoryName, pageable))
        : TransactionsDTO.of(transactionQueryRepository.getAllTransaction(userId, pageable));

    validateTransactions(transactions.getTransactions(), categoryName);

    return transactions;
  }

  private void validateTransactions(List<TransactionsDTO> transactions, String categoryName) {
    if (transactions == null || transactions.isEmpty()) {
      String message = StringUtils.hasText(categoryName)
          ? "존재하지 않은 카테고리 입니다."
          : "거래 내역에 존재하지 않습니다.";
      throw new GlobalException(HttpStatus.BAD_REQUEST, message);
    }
  }
}
