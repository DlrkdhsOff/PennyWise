package com.zero.pennywise.model.response.analyze;

import com.zero.pennywise.model.request.transaction.CategoryBalanceDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserExpensesDTO {

  private Long totalExpenses;
  private List<CategoryBalanceDTO> categoryBalances;

}
