package com.zero.pennywise.model.request.budget;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceDTO {

  private String categoryName;
  private Long balance;

}
