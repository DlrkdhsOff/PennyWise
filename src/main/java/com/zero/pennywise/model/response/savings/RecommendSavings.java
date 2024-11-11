package com.zero.pennywise.model.response.savings;

import com.zero.pennywise.model.response.balances.Balances;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendSavings {

  private String message;
  private List<Balances> balancesList;
}
