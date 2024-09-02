package com.zero.pennywise.service;

import static com.zero.pennywise.status.TransactionStatus.castToTransactionStatus;
import static com.zero.pennywise.utils.PageUtils.page;

import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.dto.transaction.TransactionDTO;
import com.zero.pennywise.model.dto.transaction.UpdateTransactionDTO;
import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.entity.WaringMessageEntity;
import com.zero.pennywise.model.response.TransactionPage;
import com.zero.pennywise.model.response.TransactionsDTO;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.UserRepository;
import com.zero.pennywise.repository.WaringMessageRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import com.zero.pennywise.status.TransactionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
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
  private final WaringMessageRepository waringMessageRepository;

  private final SseService sseService;
  private final RedisTemplate<String, Object> redisTemplate;

  // 수입/지출 등록
  public String transaction(Long userId, TransactionDTO transactionDTO) {
    UserEntity user = getUserById(userId);

    return categoriesRepository.findByCategoryName(transactionDTO.getCategoryName())
        .map(category -> {
          transactionRepository.save(
              TransactionDTO.of(user, category.getCategoryId(), transactionDTO)
          );

          updateCategoryBalanceCache(
              user,
              category.getCategoryName(),
              castToTransactionStatus(transactionDTO.getType(), transactionDTO.getIsFixed()),
              transactionDTO.getAmount()
          );
          return "거래 등록 성공";
        })

        // 사용자가 등록한 카테고리가 아닐 경우
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 카테고리 입니다."));
  }

  // 캐시 업데이트 메서드
  public void updateCategoryBalanceCache(UserEntity user, String categoryName, TransactionStatus type, Long amount) {
    String key = "lock:" + user.getId().toString();
    String value = UUID.randomUUID().toString();
    Long ttl = 30000L;

    if (!lock(key, value, ttl)) {
      throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, "캐시 업데이트 실패했습니다.");
    }
    try {
      updateCache(user, categoryName, type, amount);
    } finally {
      unlock(key, value);
    }

  }

  @SuppressWarnings("unchecked")
  private void updateCache(UserEntity user, String categoryName, TransactionStatus type, Long amount) {
    Map<String, Long> categoryBalances = (Map<String, Long>) redisTemplate.opsForHash()
        .get("categoryBalances", user.getId().toString());

    if (categoryBalances != null && categoryBalances.containsKey(categoryName)) {

      // TransactionStatus에서 연산
      Long currentValue = type.calculate(categoryBalances.get(categoryName), amount);

      // 예산 초과시 알림 설정
      if (currentValue < 0) {
        isFull(user, categoryName + " : 예산을 초과 했습니다.");
      }

      // 연산된 값으로 다시 저장
      categoryBalances.put(categoryName, currentValue);
      redisTemplate.opsForHash().put("categoryBalances", user.getId().toString(), categoryBalances);
    }
  }

  // 락을 시도하는 메서드
  private boolean lock(String key, String value, Long ttl) {
    Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, ttl, TimeUnit.MILLISECONDS);
    return Boolean.TRUE.equals(success);
  }

  // 락을 해제하는 메서드
  private void unlock(String key, String value) {
    String currentValue = (String) redisTemplate.opsForValue().get(key);
    if (value.equals(currentValue)) {
      redisTemplate.delete(key);
    }
  }

  // 예산 초과 시
  private void isFull(UserEntity user, String message) {

    sseService.sendEventToClient(user.getId().toString(), "Full!",message);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String recivedDateTime = LocalDateTime.now().format(formatter);
    waringMessageRepository.save(WaringMessageEntity.builder()
        .user(user)
        .message(message)
        .recivedDateTime(recivedDateTime)
        .build());

  }

  // 수입 / 지출 내역
  public TransactionPage getTransactionList(Long userId, String categoryName, Pageable page) {
    UserEntity user = getUserById(userId);

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
    String lastMonthsDate = getLastMonthsDate();

    List<TransactionEntity> transactions = transactionRepository
        .findByDateTimeStartingWith(lastMonthsDate);

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

  // 지난날 마지막 날짜 구하기
  public String getLastMonthsDate() {
    LocalDate lastMonthsDate = LocalDate.now().minusMonths(1);

    // 한 달 전의 마지막 날짜 계산
    YearMonth yearMonth = YearMonth.of(lastMonthsDate.getYear(), lastMonthsDate.getMonth());
    LocalDate lastDayOfLastMonth = yearMonth.atEndOfMonth();

    // 마지막 날에 맞춰 날짜를 조정
    if (lastMonthsDate.getDayOfMonth() > lastDayOfLastMonth.getDayOfMonth()) {
      lastMonthsDate = lastDayOfLastMonth;
    }
    return lastMonthsDate.toString();
  }


  // 거래 목록 수정
  public String updateTransaction(Long userId, UpdateTransactionDTO updateTransactionDTO) {
    if (!transactionRepository.existsByUserId(userId)) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "거래내역이 존재하지 않습니다.");
    }

    TransactionEntity transaction = getTransaction(updateTransactionDTO.getTransactionId());

    CategoriesEntity categories = getCategoryByName(updateTransactionDTO.getCategoryName());

    transaction.setCategoryId(categories.getCategoryId());
    transaction.setType(castToTransactionStatus(updateTransactionDTO.getType(), updateTransactionDTO.getIsFixed()));
    transaction.setAmount(updateTransactionDTO.getAmount());
    transaction.setDescription(updateTransactionDTO.getDescription());
    transactionRepository.save(transaction);

    return "거래 정보를 수정하였습니다.";
  }


  // 거래 삭제
  public String deleteTransaction(Long userId, Long trasactionId) {
    UserEntity user = getUserById(userId);

    TransactionEntity transaction = getTransaction(trasactionId);

    transactionRepository.deleteByUserIdAndTransactionId(user.getId(),
        transaction.getTransactionId());

    return "거래를 성공적으로 삭제 하였습니다.";
  }


  // 공통 메서드

  // 사용자 조회
  private UserEntity getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."));
  }

  // 거래 조회
  private TransactionEntity getTransaction(Long transactionId) {
    return transactionRepository.findByTransactionId(transactionId)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 거래 아이디 입니다."));
  }

  // 카테고리 조회
  private CategoriesEntity getCategoryByName(String categoryName) {
    return categoriesRepository.findByCategoryName(categoryName)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않는 카테고리입니다."));
  }
}
