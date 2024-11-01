package com.zero.pennywise.model.response.balances;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Balances {

  private String categoryName;
  private Long amount;
  private Long balance;

}
