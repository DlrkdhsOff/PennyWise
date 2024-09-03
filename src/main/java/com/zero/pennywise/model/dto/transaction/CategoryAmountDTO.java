package com.zero.pennywise.model.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAmountDTO {

  private String categoryName;
  private Long totalIncome;
  private Long totalExpenses;

}
