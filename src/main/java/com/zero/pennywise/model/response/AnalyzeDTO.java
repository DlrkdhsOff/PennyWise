package com.zero.pennywise.model.response;

import com.zero.pennywise.model.request.transaction.CategoryBalanceDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyzeDTO {

  private Long totalExpenses;
  private List<CategoryBalanceDTO> categoryBalances;
}
