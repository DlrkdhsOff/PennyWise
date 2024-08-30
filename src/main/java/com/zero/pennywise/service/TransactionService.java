package com.zero.pennywise.service;

import static com.zero.pennywise.status.TransactionStatus.castToTransactionStatus;
import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.TransactionDTO;
import com.zero.pennywise.model.dto.UpdateTransactionDTO;
import com.zero.pennywise.model.entity.CategoriesEntity;
import com.zero.pennywise.model.entity.TransactionEntity;
import com.zero.pennywise.model.entity.UserEntity;
import com.zero.pennywise.model.response.TransactionPage;
import com.zero.pennywise.model.response.TransactionsDTO;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.querydsl.TransactionQueryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
  private final UserRepository userRepository;
  private final TransactionQueryRepository transactionQueryRepository;

  // 수입/지출 등록
  public String transaction(Long userId, TransactionDTO transactionDTO) {
    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 회원입니다."));

    return categoriesRepository.findByCategoryName(transactionDTO.getCategoryName())
        .map(category -> {
          transactionRepository.save(
              TransactionDTO.of(user, category.getCategoryId(), transactionDTO)
          );
          return "거래 등록 성공";
        })

        // 사용자가 등록한 카테고리가 아닐 경우
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다."));
  }

  // 수입 / 지출 내역
  public TransactionPage getTransactionList(Long userId, String categoryName, Pageable page) {
    UserEntity user = userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 회원입니다."));

    Pageable pageable = page(page);

    TransactionPage transactions = (StringUtils.hasText(categoryName))
        ? TransactionsDTO.of(
        transactionQueryRepository.getTransactionsByCategory(user, categoryName, pageable))
        : TransactionsDTO.of(transactionQueryRepository.getAllTransaction(user, pageable));

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


  // 매일 00시에 자동으로 현재를 기준으로 고정 수입/지출 자동 등록
  public void updateFixedTransaction() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 오늘 날짜
    String todayDate = LocalDateTime.now().format(formatter);
    // 한 달 전 날짜
    LocalDate lastMonthsDate = LocalDate.now().minusMonths(1);

    // 한 달 전의 마지막 날짜 계산
    YearMonth yearMonth = YearMonth.of(lastMonthsDate.getYear(), lastMonthsDate.getMonth());
    LocalDate lastDayOfLastMonth = yearMonth.atEndOfMonth();

    // 마지막 날에 맞춰 날짜를 조정
    if (lastMonthsDate.getDayOfMonth() > lastDayOfLastMonth.getDayOfMonth()) {
      lastMonthsDate = lastDayOfLastMonth;
    }

    List<TransactionEntity> transactions = transactionRepository
        .findByDateTimeStartingWith(lastMonthsDate.toString());

    for (TransactionEntity transaction : transactions) {
      transactionRepository.save(TransactionEntity.builder()
          .user(transaction.getUser())
          .categoryId(transaction.getCategoryId())
          .type(transaction.getType())
          .amount(transaction.getAmount())
          .description(transaction.getDescription())
          .dateTime(todayDate)
          .build());
    }
  }


  // 거래 목록 수정
  public String updateTransaction(Long userId, UpdateTransactionDTO updateTransactionDTO) {
    if (!transactionRepository.existsByUserId(userId)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "거래내역이 존재하지 않습니다.");
    }

    TransactionEntity transaction = transactionRepository.findByTransactionId(updateTransactionDTO.getTransactionId())
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 거래 아이디 입니다."));

    CategoriesEntity categories = categoriesRepository.findByCategoryName(updateTransactionDTO.getCategoryName())
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 카테고리 입니다."));

    transaction.setCategoryId(categories.getCategoryId());
    transaction.setType(castToTransactionStatus(updateTransactionDTO.getType(), updateTransactionDTO.getIsFixed()));
    transaction.setAmount(updateTransactionDTO.getAmount());
    transaction.setDescription(updateTransactionDTO.getDescription());
    transactionRepository.save(transaction);

    return "거래 정보를 수정하였습니다.";
  }

}
