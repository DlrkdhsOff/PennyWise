package com.zero.pennywise.model.dto.budget;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BudgetDTO {

  @NotBlank(message = "카테고리를 입력해주세요.")
  private String categoryName;

  @NotNull(message = "예산을 설정해주세요.")
  private Long amount;
}
