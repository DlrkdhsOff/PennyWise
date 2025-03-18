package com.zero.pennywise.domain.model.request.budget;

import com.zero.pennywise.domain.entity.BudgetEntity;
import com.zero.pennywise.domain.entity.CategoryEntity;
import com.zero.pennywise.domain.entity.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetDTO {

  @NotEmpty(message = "카테고리를 입력해주세요.")
  private String categoryName;

  @NotNull(message = "예산을 설정해주세요.")
  private Long amount;

  public static BudgetEntity of(UserEntity user, CategoryEntity category, BudgetDTO budgetDTO) {
    return BudgetEntity.builder()
        .user(user)
        .category(category)
        .amount(budgetDTO.getAmount())
        .build();
  }
}
