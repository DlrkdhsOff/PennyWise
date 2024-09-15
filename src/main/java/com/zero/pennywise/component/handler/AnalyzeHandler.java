package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.entity.WaringMessageEntity;
import com.zero.pennywise.model.request.transaction.CategoryBalanceDTO;
import com.zero.pennywise.model.response.analyze.AnalyzeDTO;
import com.zero.pennywise.model.response.waring.WaringMessageDTO;
import com.zero.pennywise.repository.WaringMessageRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyzeHandler {

  private final TransactionQueryRepository transactionQueryRepository;
  private final RedisTemplate<String, Object> redisTemplate;
  private final WaringMessageRepository waringMessageRepository;
  private final CategoryHandler categoryHandler;

  // 이번달 전체 지출 금액 및 카테고리별 지출 금액
  public AnalyzeDTO getThisMonthBalance(Long userId) {
    List<CategoryEntity> categories = categoryHandler.getAllCategoryList(userId);

    String thisMonth = LocalDate.now().toString();

    Long totalEexpenses = transactionQueryRepository
        .getExpenses(userId, null, thisMonth);

    List<CategoryBalanceDTO> list = new ArrayList<>();
    for (CategoryEntity c : categories) {
      list.add(new CategoryBalanceDTO(
          c.getCategoryName(),
          transactionQueryRepository.getExpenses(userId, c.getCategoryId(), thisMonth)
      ));
    }

    return new AnalyzeDTO(totalEexpenses, list);
  }

  // 지난 3달 지출 총액 평균 및 카테고리별 지출 총액 평균
  public AnalyzeDTO getLastThreeMonthBalance(Long userId) {
    List<CategoryEntity> categories = categoryHandler.getAllCategoryList(userId);

    LocalDateTime startDateTime = getStartDate();
    LocalDateTime endDateTime = getLastMonth();

    Long totalEexpenses = transactionQueryRepository
        .getExpensesAvgLastThreeMonth(userId, null, startDateTime, endDateTime);

    List<CategoryBalanceDTO> list = new ArrayList<>();
    for (CategoryEntity c : categories) {
      list.add(new CategoryBalanceDTO(
          c.getCategoryName(),
          transactionQueryRepository
              .getExpensesAvgLastThreeMonth(userId, c.getCategoryId(), startDateTime, endDateTime)
      ));
    }

    list.sort(Comparator.comparingLong(CategoryBalanceDTO::getTotalExpenses).reversed());
    return new AnalyzeDTO(totalEexpenses, list);
  }


  public Long getTotalIncome(Long userId) {
    LocalDateTime startDateTime = getStartDate();
    LocalDateTime endDateTime = getLastMonth();

    return transactionQueryRepository.getIncomeAvgLastThreeMonth(userId, startDateTime, endDateTime);
  }


  private LocalDateTime getStartDate() {
    return LocalDateTime.now()
        .minusMonths(4)
        .withDayOfMonth(1)
        .withHour(0)
        .withMinute(0)
        .withSecond(0);
  }

  private LocalDateTime getLastMonth() {
    YearMonth lastMonth = YearMonth.from(LocalDateTime.now().minusMonths(1));
    return lastMonth.atEndOfMonth().atTime(23, 59, 59);
  }


  // 경고 메시지 전송
  public void sendMessage(UserEntity user, String message) {
    WaringMessageEntity warningMessage = WaringMessageEntity.builder()
        .user(user)
        .message(message)
        .recivedDateTime(LocalDateTime.now())
        .build();

    waringMessageRepository.save(warningMessage);
    redisTemplate.convertAndSend("notifications", new WaringMessageDTO(user.getId(), message));
  }

}
