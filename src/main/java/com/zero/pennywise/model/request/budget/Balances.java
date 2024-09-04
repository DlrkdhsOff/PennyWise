package com.zero.pennywise.model.request.budget;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Balances {

  private String categoryName;
  private Long amount;
  private Long balance;

}
