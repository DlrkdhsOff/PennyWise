package com.zero.pennywise.model.request.budget;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BalancesDTO {

  private String categoryName;
  private Long amount;
  private Long balance;

}
