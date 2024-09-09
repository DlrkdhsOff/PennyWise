package com.zero.pennywise.model.request.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryBalanceDTO {

  private String categoryName;
  private Long totalExpenses;

}
