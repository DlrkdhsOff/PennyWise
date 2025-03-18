package com.zero.pennywise.domain.model.request.budget;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateBudgetDTO {

  @NotEmpty(message = "카테고리를 입력해주세요.")
  private String categoryName;

  @NotNull(message = "예산을 설정해주세요.")
  private Long amount;
}
