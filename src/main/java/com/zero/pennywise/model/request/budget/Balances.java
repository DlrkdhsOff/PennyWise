package com.zero.pennywise.model.request.budget;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balances {

  private String categoryName;
  private Long amount;
  private Long balance;

}
