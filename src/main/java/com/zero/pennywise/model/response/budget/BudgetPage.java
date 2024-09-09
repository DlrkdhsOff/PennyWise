package com.zero.pennywise.model.response.budget;

import com.zero.pennywise.model.request.budget.BalancesDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class BudgetPage {

  private int pageNumber;
  private int totalPage;
  private Long totalData;
  private List<BalancesDTO> budgetList;

  public static BudgetPage of(Page<BalancesDTO> pageList) {
    return new BudgetPage(
        pageList.getNumber() + 1,
        pageList.getTotalPages(),
        pageList.getTotalElements(),
        pageList.getContent()
    );

  }

}
