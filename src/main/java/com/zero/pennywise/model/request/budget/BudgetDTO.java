package com.zero.pennywise.model.request.budget;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetDTO {

  @NotEmpty(message = "카테고리를 입력해주세요.")
  private String categoryName;

  @NotEmpty(message = "예산을 설정해주세요.")
  private Long amount;
}
