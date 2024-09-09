package com.zero.pennywise.component.handler;

import com.zero.pennywise.entity.CategoriesEntity;
import com.zero.pennywise.entity.SavingsEntity;
import com.zero.pennywise.entity.UserEntity;
import com.zero.pennywise.model.request.savings.SavingsDTO;
import com.zero.pennywise.repository.CategoriesRepository;
import com.zero.pennywise.repository.SavingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavingHandler {

  private final SavingsRepository savingsRepository;
  private final CategoriesRepository categoriesRepository;

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

}
