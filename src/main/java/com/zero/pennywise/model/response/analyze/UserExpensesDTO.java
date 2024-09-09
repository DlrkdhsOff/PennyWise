package com.zero.pennywise.model.response.analyze;

import com.zero.pennywise.model.request.transaction.CategoryBalanceDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserExpensesDTO {

  private Long totalExpenses;
  private List<CategoryBalanceDTO> categoryBalances;

}
