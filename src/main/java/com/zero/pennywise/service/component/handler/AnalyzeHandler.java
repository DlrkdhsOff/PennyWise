package com.zero.pennywise.service.component.handler;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.entity.WaringMessageEntity;
import com.zero.pennywise.model.request.transaction.CategoryBalanceDTO;
import com.zero.pennywise.model.response.analyze.AnalyzeDTO;
import com.zero.pennywise.model.response.waring.WaringMessageDTO;
import com.zero.pennywise.repository.WaringMessageRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import com.zero.pennywise.service.component.redis.CategoryCache;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyzeHandler {

  private final TransactionQueryRepository transactionQueryRepository;
  private final CategoryCache categoryCache;
  private final RedisTemplate<String, Object> redisTemplate;
  private final WaringMessageRepository waringMessageRepository;

  // 이번달 전체 지출 금액 및 카테고리별 지출 금액
  public AnalyzeDTO getThisMonthBalance(Long userId) {
    List<CategoriesEntity> categories = categoryCache
        .getCategoriesFromCache(userId);

    String thisMonth = LocalDate.now().toString();

    Long totalEexpenses = transactionQueryRepository
        .getExpenses(userId, null, thisMonth);

    List<CategoryBalanceDTO> list = new ArrayList<>();
    for (CategoriesEntity c : categories) {
      list.add(new CategoryBalanceDTO(
          c.getCategoryName(),
          transactionQueryRepository.getExpenses(userId, c.getCategoryId(), thisMonth)
      ));
    }

    return new AnalyzeDTO(totalEexpenses, list);
  }

  // 지난 3달 지출 총액 평균 및 카테고리별 지출 총액 평균
  public AnalyzeDTO getLastThreeMonthBalance(Long userId) {
    List<CategoriesEntity> categories = categoryCache.getCategoriesFromCache(userId);

    LocalDateTime startDateTime = getStartDate();
    LocalDateTime endDateTime = getLastMonth();

    Long totalEexpenses = transactionQueryRepository
        .getTracsactionAvgLastThreeMonth(userId, null, startDateTime, endDateTime);

    List<CategoryBalanceDTO> list = new ArrayList<>();
    for (CategoriesEntity c : categories) {
      list.add(new CategoryBalanceDTO(
          c.getCategoryName(),
          transactionQueryRepository
              .getTracsactionAvgLastThreeMonth(userId, c.getCategoryId(), startDateTime, endDateTime)
      ));
    }

    return new AnalyzeDTO(totalEexpenses, list);
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
