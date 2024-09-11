package com.zero.pennywise.service;

import com.zero.pennywise.component.handler.AnalyzeHandler;
import com.zero.pennywise.component.handler.SavingHandler;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.savings.DeleteSavingsDTO;
import com.zero.pennywise.model.request.savings.SavingsDTO;
import com.zero.pennywise.model.response.analyze.AnalyzeDTO;
import com.zero.pennywise.model.response.savings.SavingsPage;
import com.zero.pennywise.repository.querydsl.savings.SavingsQueryRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavingsService {

  private final UserHandler userHandler;
  private final SavingHandler savingHandler;
  private final SavingsQueryRepository savingsQueryRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final AnalyzeHandler analyzeHandler;


  // 저축 정보 등록
  public String setSavings(Long userId, SavingsDTO savingsDTO) {
    UserEntity user = userHandler.getUserById(userId);

    if (LocalDate.now().isAfter(savingsDTO.getStartDate())) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "지난 날짜는 입력 할 수 없습니다.");
    }

    if (savingsDTO.getMonthsToSave() < 3) {
      throw new GlobalException(HttpStatus.BAD_REQUEST, "최소 3개월 부터 등록 할 수 있습니다.");
    }

    savingHandler.save(user, savingsDTO);

    return "성공적으로 저축 정보를 등록하였습니다. ";
  }

  // 저축 정보 조회
  public SavingsPage getSavings(Long userId, Pageable page) {
    UserEntity user = userHandler.getUserById(userId);

    CategoryEntity category = savingHandler.getCategory(user);

    return SavingsPage.of(savingsQueryRepository
        .getAllSavings(user.getId(), page, category.getCategoryId()));
  }

  // 저축 정보 삭제
  public String deleteSavings(Long userId, DeleteSavingsDTO deleteSavingsDTO) {
    UserEntity user = userHandler.getUserById(userId);

    SavingsEntity savings = savingHandler.getSavings(user, deleteSavingsDTO.getName());
    savingHandler.endDeposit(user, savings);

    return "저축 정보를 삭제 하였습니다.";
  }


  public Object recommend(Long userId) {
    StringBuilder sb = new StringBuilder();
    UserEntity user = userHandler.getUserById(userId);

    LocalDateTime startDateTime = getStartDate();
    LocalDateTime endDateTime = getLastMonth();

    Long totalIncome = transactionQueryRepository
        .getFixedIncomeAvgLastThreeMonth(userId, startDateTime, endDateTime);

    AnalyzeDTO lastThreeMonth = analyzeHandler.getLastThreeMonthBalance(userId);

    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.KOREA);
    if (totalIncome * 0.2 < lastThreeMonth.getTotalExpenses()) {
      sb.append(user.getUsername())
          .append("님 월 평균 소득 : ")
          .append(numberFormat.format(totalIncome))
          .append("원\n");
      sb.append(user.getUsername())
          .append("님 월 평균 지출 : ")
          .append(numberFormat.format(lastThreeMonth.getTotalExpenses()))
          .append("원\n");

      sb.append("추천 저축 금액은 소득의 20% 입니다.\n");


    }
    return null;
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

}
