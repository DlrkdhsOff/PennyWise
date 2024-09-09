package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.TransactionEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.exception.GlobalException;
import com.zero.pennywise.model.request.savings.SavingsDTO;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.SavingsRepository;
import com.zero.pennywise.repository.TransactionRepository;
import com.zero.pennywise.repository.querydsl.transaction.TransactionQueryRepository;
import com.zero.pennywise.status.TransactionStatus;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavingHandler {

  private final SavingsRepository savingsRepository;
  private final CategoriesRepository categoriesRepository;
  private final TransactionQueryRepository transactionQueryRepository;
  private final TransactionRepository transactionRepository;

  public SavingsEntity save(UserEntity user, SavingsDTO savingsDTO) {
    return savingsRepository.save(SavingsEntity.builder()
        .user(user)
        .name(savingsDTO.getName())
        .amount(savingsDTO.getAmount())
        .startDate(savingsDTO.getStartDate())
        .endDate(savingsDTO.getStartDate().plusMonths(savingsDTO.getMonthsToSave()))
        .description(savingsDTO.getDescription())
        .build());
  }


  public CategoriesEntity getOrCreateCategory(UserEntity user) {
    return categoriesRepository.findByUserIdAndCategoryName(user.getId(), "저축")
        .orElseGet(() -> categoriesRepository.save(CategoriesEntity.builder()
            .user(user)
            .categoryName("저축")
            .build()));
  }

  public SavingsEntity getSavings(UserEntity user, String name) {
    return savingsRepository.findByUserIdAndName(user.getId(), name)
        .orElseThrow(() -> new GlobalException(HttpStatus.BAD_REQUEST, "존재하지 않은 저축 정보 입니다."));
  }

  public void payment(UserEntity user, SavingsEntity savings) {
    CategoriesEntity category = getOrCreateCategory(user);

    transactionRepository.save(TransactionEntity.builder()
        .user(user)
        .amount(savings.getAmount())
        .type(TransactionStatus.FIXED_EXPENSES)
        .categoryId(category.getCategoryId())
        .dateTime(LocalDateTime.now())
        .description(savings.getName() + savings.getDescription())
        .build());
  }

  public void endDeposit(UserEntity user, SavingsEntity savings) {

    CategoriesEntity category = getOrCreateCategory(user);
    String description = savings.getName() + savings.getDescription();

    Long currentAmount = transactionQueryRepository
        .getCurrentAmount(user, category.getCategoryId(), description);

    transactionRepository.save(TransactionEntity.builder()
        .user(user)
        .categoryId(category.getCategoryId())
        .type(TransactionStatus.INCOME)
        .amount(currentAmount)
        .description(description)
        .dateTime(LocalDateTime.now())
        .build());
  }
}
