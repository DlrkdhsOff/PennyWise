package com.zero.pennywise.model.request.budget;

import com.zero.pennywise.entity.BudgetEntity;
import com.zero.pennywise.entity.CategoryEntity;
import com.zero.pennywise.entity.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateBudgetDTO {

  @NotNull(message = "예산 아이디를 입력해주세요.")
  private Long budgetId;

  @NotEmpty(message = "카테고리를 입력해주세요.")
  private String categoryName;

  @NotNull(message = "예산을 설정해주세요.")
  private Long amount;

  public static BudgetEntity of(UserEntity user, CategoryEntity category, UpdateBudgetDTO updateBudgetDTO) {
    return BudgetEntity.builder()
        .budgetId(updateBudgetDTO.getBudgetId())
        .user(user)
        .category(category)
        .amount(updateBudgetDTO.getAmount())
        .build();
  }
}
