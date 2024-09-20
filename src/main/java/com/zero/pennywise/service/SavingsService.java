package com.zero.pennywise.service;

import com.zero.pennywise.component.handler.AnalyzeHandler;
import com.zero.pennywise.component.handler.SavingHandler;
import com.zero.pennywise.component.handler.UserHandler;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.enums.RecommendMessage;
import com.zero.pennywise.model.request.savings.DeleteSavingsDTO;
import com.zero.pennywise.model.request.savings.SavingsDTO;
import com.zero.pennywise.model.response.analyze.AnalyzeDTO;
import com.zero.pennywise.model.response.savings.SavingsPage;
import com.zero.pennywise.repository.querydsl.savings.SavingsQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavingsService {

  private final UserHandler userHandler;
  private final SavingHandler savingHandler;
  private final SavingsQueryRepository savingsQueryRepository;
  private final AnalyzeHandler analyzeHandler;


  // 저축 정보 등록
  public String setSavings(Long userId, SavingsDTO savingsDTO) {
    UserEntity user = userHandler.getUserById(userId);

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

  public String recommend(Long userId) {
    UserEntity user = userHandler.getUserById(userId);

    Long totalIncome = analyzeHandler.getTotalIncome(userId);
    AnalyzeDTO analyzeDTO = analyzeHandler.getLastThreeMonthBalance(userId);

    Long totalExpenses = analyzeDTO.getTotalExpenses();
    List<SavingsEntity> savingsList = savingHandler.getAllSavings(userId);

    Long savingAccount = null;
    if (savingsList != null) {
      savingAccount = savingsList.stream().mapToLong(SavingsEntity::getAmount).sum();
    }

    String maxExpenseCategory = analyzeDTO.getCategoryBalances().get(0).getCategoryName();

    return RecommendMessage.getMessage(totalIncome, totalExpenses, savingAccount, maxExpenseCategory);
  }


}
